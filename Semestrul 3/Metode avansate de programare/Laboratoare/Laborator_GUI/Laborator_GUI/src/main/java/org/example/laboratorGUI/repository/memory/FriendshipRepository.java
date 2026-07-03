package org.example.laboratorGUI.repository.memory;

import org.example.laboratorGUI.domain.friendship.Friendship;
import org.example.laboratorGUI.exceptions.RepositoryException;
import org.example.laboratorGUI.utils.types.TipFriendship;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class FriendshipRepository {
    private final Map<Long, List<Friendship>> friendships = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Saves a new friendship between two users.
     * If a friendship already exists between the users, a RepositoryException is thrown.
     *
     * @param userId1 the id of the first user
     * @param userId2 the id of the second user
     * @throws RepositoryException if the users are already friends
     */
    public void save(Long userId1, Long userId2){
        long id = idGenerator.getAndIncrement();
        Friendship f = new Friendship(id, userId1, userId2, TipFriendship.PENDING);
        boolean exists = friendships.values().stream()
                .flatMap(List::stream)
                .anyMatch(existing -> existing.equals(f));

        if (exists)
            throw new RepositoryException("The users are already friends!");

        friendships.computeIfAbsent(userId1, k -> new ArrayList<>()).add(f);
        friendships.computeIfAbsent(userId2, k -> new ArrayList<>()).add(f);
    }

    /**
     * Builds the adjacency list for the friendship graph.
     *
     * @return a map where keys are user IDs and values are lists of friend IDs
     * @throws RepositoryException if no friendships exist
     */
    public Map<Long, List<Long>> buildAdjacency(){
        Map<Long, List<Long>> adj = new HashMap<>();
        if (friendships.isEmpty())
            throw new RepositoryException("No friendships found!");
        for (var id : friendships.keySet())
            adj.put(id, new ArrayList<>());
        for (var id : friendships.keySet()) {
            var friends = getFriendsIds(id);
            if (friends.isEmpty())
                continue;
            adj.get(id).addAll(friends);
        }
        return adj;
    }

    /**
     * Returns a list of user IDs that are friends with the given user.
     * If the user has no friends, returns an empty list.
     *
     * @param userId the id of the user
     * @return a list of user IDs who are friends with the given user
     */
    public List<Long> getFriendsIds(Long userId){
        List<Long> ids = new ArrayList<>();
        List<Friendship> friends = friendships.get(userId);
        if (friends == null)
            return ids;

        for (Friendship f : friends) {
            Long friendId = userId.equals(f.getUserId1()) ? f.getUserId2() : f.getUserId1();
            if (!ids.contains(friendId))
                ids.add(friendId);
        }
        return ids;
    }

    /**
     * Deletes the friendship between two users.
     * If either user ID does not exist in the repository, a RepositoryException is thrown.
     *
     * @param userId1 the id of the first user
     * @param userId2 the id of the second user
     * @throws RepositoryException if one of the user IDs is invalid
     */
    public void delete(Long userId1, Long userId2){
        if (friendships.containsKey(userId1) && friendships.containsKey(userId2)) {
            friendships.get(userId1).removeIf(f -> f.getUserId1() == userId2 || f.getUserId2() == userId2);
            friendships.get(userId2).removeIf(f -> f.getUserId1() == userId1 || f.getUserId2() == userId1);
        }
        else
            throw new RepositoryException("Invalid ids!");
    }

    /**
     * Deletes a user from the repository, removing all friendships associated with that user.
     *
     * @param userId the id of the user to be deleted
     */
    public void deleteUser(Long userId){
        friendships.remove(userId);
        for (var friendsList : friendships.values())
            friendsList.removeIf(f -> f.getUserId1() == userId || f.getUserId2() == userId);
    }

    /**
     * Checks if two users are friends.
     *
     * @param userId1 the id of the first user
     * @param userId2 the id of the second user
     * @return true if the users are friends, false otherwise
     */
    public boolean checkIfTheyAreFriends(Long userId1, Long userId2){
        if (friendships.containsKey(userId1) && friendships.containsKey(userId2)) {
            for (Friendship f : friendships.get(userId1))
                if (f.getUserId1() == userId2 || f.getUserId2() == userId2)
                    return true;
        }
        return false;
    }

    /**
     * Returns a set of all user IDs that have at least one friendship.
     *
     * @return a set of user IDs who have friends
     */
    public Set<Long> findUsersWithFriends() {
        return friendships.keySet();
    }
}
