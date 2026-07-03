package org.example.ducksocialnetworkui.service;

import org.example.ducksocialnetworkui.domain.Friendship;
import org.example.ducksocialnetworkui.domain.FriendshipId;
import org.example.ducksocialnetworkui.event.EntityChangeEvent;
import org.example.ducksocialnetworkui.event.EntityChangeEventType;
import org.example.ducksocialnetworkui.exception.ServiceException;
import org.example.ducksocialnetworkui.observer.Observable;
import org.example.ducksocialnetworkui.observer.Observer;
import org.example.ducksocialnetworkui.repository.FriendshipRepository;
import org.example.ducksocialnetworkui.utils.paging.Page;
import org.example.ducksocialnetworkui.utils.paging.Pageable;

import java.util.*;
import java.util.stream.StreamSupport;

public class FriendshipService implements Observable<EntityChangeEvent<Friendship>> {

    private final FriendshipRepository friendshipRepository;
    private final List<Observer<EntityChangeEvent<Friendship>>> observers = new ArrayList<>();

    public FriendshipService(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    private FriendshipId normalize(Long a, Long b) {
        return new FriendshipId(Math.min(a, b), Math.max(a, b));
    }

    public void addFriendship(Long idA, Long idB) {

        if (idA.equals(idB))
            throw new ServiceException("Un user nu poate fi prieten cu el insusi.");

        FriendshipId fid = normalize(idA, idB);

        if (friendshipRepository.findOne(fid).isPresent()) {
            throw new ServiceException("Prietenia exista deja.");
        }

        try {
            Optional<Friendship> saved = friendshipRepository.save(new Friendship(idA, idB));

            Friendship f = saved.orElseThrow(() ->
                    new ServiceException("Eroare la salvarea prieteniei."));

            notifyObservers(new EntityChangeEvent<>(EntityChangeEventType.ADD, f));

        } catch (Exception e) {
            throw new ServiceException("Utilizatorii nu exista sau relatia este invalida.");
        }
    }

    public void removeFriendship(Long idA, Long idB) {
        FriendshipId fid = normalize(idA, idB);

        Optional<Friendship> existing = friendshipRepository.findOne(fid);

        if (existing.isEmpty()) {
            throw new ServiceException("Nu exista prietenie intre cei doi utilizatori.");
        }

        Friendship removed = friendshipRepository.delete(fid)
                .orElseThrow(() -> new ServiceException("Eroare la stergerea prieteniei."));

        notifyObservers(new EntityChangeEvent<>(EntityChangeEventType.DELETE, null, removed));
    }

    public Iterable<Friendship> getFriendships() {
        return friendshipRepository.findAll();
    }

    public Page<Friendship> getAllPage(Pageable page) {
        return friendshipRepository.getAllPage(page);
    }

    private Map<Long, Set<Long>> buildGraph() {
        Map<Long, Set<Long>> g = new HashMap<>();

        for (Friendship f : friendshipRepository.findAll()) {
            Long a = f.getUserId1();
            Long b = f.getUserId2();

            g.putIfAbsent(a, new HashSet<>());
            g.putIfAbsent(b, new HashSet<>());

            g.get(a).add(b);
            g.get(b).add(a);
        }

        return g;
    }

    private List<Set<Long>> getComponents(Map<Long, Set<Long>> g) {
        List<Set<Long>> comps = new ArrayList<>();
        Set<Long> visited = new HashSet<>();

        for (Long start : g.keySet()) {
            if (visited.contains(start)) continue;

            Set<Long> comp = new HashSet<>();
            Deque<Long> q = new ArrayDeque<>();
            q.add(start);
            visited.add(start);

            while (!q.isEmpty()) {
                Long cur = q.remove();
                comp.add(cur);

                for (Long neigh : g.get(cur)) {
                    if (!visited.contains(neigh)) {
                        visited.add(neigh);
                        q.add(neigh);
                    }
                }
            }

            comps.add(comp);
        }

        return comps;
    }

    public int getNumberOfCommunities() {
        return getComponents(buildGraph()).size();
    }

    private int computeDiameter(Set<Long> comp, Map<Long, Set<Long>> g) {
        int diameter = 0;

        for (Long src : comp) {
            Map<Long, Integer> dist = new HashMap<>();
            Deque<Long> q = new ArrayDeque<>();
            q.add(src);
            dist.put(src, 0);

            while (!q.isEmpty()) {
                Long v = q.remove();
                int d = dist.get(v);

                for (Long neigh : g.get(v)) {
                    if (comp.contains(neigh) && !dist.containsKey(neigh)) {
                        dist.put(neigh, d + 1);
                        q.add(neigh);
                    }
                }
            }

            diameter = Math.max(diameter, Collections.max(dist.values()));
        }

        return diameter;
    }

    public Set<Long> getMostSociableCommunity() {
        Map<Long, Set<Long>> g = buildGraph();
        List<Set<Long>> comps = getComponents(g);

        int best = -1;
        Set<Long> bestComp = Set.of();

        for (Set<Long> comp : comps) {
            int diam = computeDiameter(comp, g);
            if (diam > best) {
                best = diam;
                bestComp = comp;
            }
        }

        return bestComp;
    }

    public int getNumberOfFriends(Long userId) {
        return (int) StreamSupport
                .stream(friendshipRepository.findAll().spliterator(), false)
                .filter(f ->
                        f.getUserId1().equals(userId) ||
                                f.getUserId2().equals(userId)
                )
                .count();
    }


    @Override
    public void addObserver(Observer<EntityChangeEvent<Friendship>> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<EntityChangeEvent<Friendship>> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(EntityChangeEvent<Friendship> e) {
        observers.forEach(o -> o.update(e));
    }
}
