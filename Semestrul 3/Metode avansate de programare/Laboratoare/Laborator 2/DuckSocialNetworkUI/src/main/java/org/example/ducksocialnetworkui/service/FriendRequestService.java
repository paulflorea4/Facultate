package org.example.ducksocialnetworkui.service;

import org.example.ducksocialnetworkui.domain.FriendRequest;
import org.example.ducksocialnetworkui.domain.FriendRequestStatus;
import org.example.ducksocialnetworkui.event.EntityChangeEvent;
import org.example.ducksocialnetworkui.event.EntityChangeEventType;
import org.example.ducksocialnetworkui.exception.ServiceException;
import org.example.ducksocialnetworkui.observer.Observable;
import org.example.ducksocialnetworkui.observer.Observer;
import org.example.ducksocialnetworkui.repository.FriendRequestRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestService implements Observable<EntityChangeEvent<FriendRequest>> {
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipService friendshipService;
    private final List<Observer<EntityChangeEvent<FriendRequest>>> observers=new ArrayList<>();

    public FriendRequestService(FriendRequestRepository friendRequestRepository, FriendshipService friendshipService) {
        this.friendRequestRepository = friendRequestRepository;
        this.friendshipService = friendshipService;
    }

    public void send(Long from,Long to){
        if(from.equals(to)){
            throw new ServiceException("Nu te poti adauga pe tine!");
        }

        if(friendRequestRepository.findByUsers(from,to).isPresent()){
            throw new ServiceException("Cererea exista deja!");
        }

        FriendRequest fr = new FriendRequest(null,from,to, FriendRequestStatus.PENDING, LocalDateTime.now());

        friendRequestRepository.save(fr).ifPresent(r -> notifyObservers(new EntityChangeEvent<>(
                EntityChangeEventType.FRIEND_REQUEST_RECEIVED,r)
        ));
    }

    public void respond(Long requestId, boolean accept) {
        FriendRequestStatus status =
                accept ? FriendRequestStatus.APPROVED : FriendRequestStatus.REJECTED;

        FriendRequest fr = friendRequestRepository.updateStatus(requestId, status)
                .orElseThrow(() -> new ServiceException("Cererea nu există"));

        if (status == FriendRequestStatus.APPROVED)
            friendshipService.addFriendship(fr.getFromId(), fr.getToId());

        notifyObservers(new EntityChangeEvent<>(
                EntityChangeEventType.FRIEND_REQUEST_UPDATED, fr
        ));
    }

    public List<FriendRequest> getAllForUser(Long userId) {
        return friendRequestRepository.findAllForUser(userId);
    }

    public List<FriendRequest> getPending(Long userId) {
        return friendRequestRepository.findPendingForUser(userId);
    }

    @Override
    public void addObserver(Observer<EntityChangeEvent<FriendRequest>> o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers(EntityChangeEvent<FriendRequest> e) {
        observers.forEach(o -> o.update(e));
    }

    @Override
    public void removeObserver(Observer<EntityChangeEvent<FriendRequest>> o) {
        observers.remove(o);
    }
}
