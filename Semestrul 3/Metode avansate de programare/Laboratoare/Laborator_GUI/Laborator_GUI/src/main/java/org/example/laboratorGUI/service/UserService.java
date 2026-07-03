package org.example.laboratorGUI.service;

import org.example.laboratorGUI.domain.user.User;
import org.example.laboratorGUI.domain.flock.*;
import org.example.laboratorGUI.domain.flock.FlyingFlock;
import org.example.laboratorGUI.domain.flock.SwimmingFlock;
import org.example.laboratorGUI.domain.user.duck.Duck;
import org.example.laboratorGUI.domain.user.duck.FlyingDuck;
import org.example.laboratorGUI.domain.user.duck.FlyingSwimmingDuck;
import org.example.laboratorGUI.domain.user.duck.SwimmingDuck;
import org.example.laboratorGUI.domain.friendship.Friendship;
import org.example.laboratorGUI.domain.user.person.Person;
import org.example.laboratorGUI.exceptions.RepositoryException;
import org.example.laboratorGUI.exceptions.ServiceException;
import org.example.laboratorGUI.factory.DuckFactory;
import org.example.laboratorGUI.repository.database.DBFlockRepository;
import org.example.laboratorGUI.repository.database.DBFriendshipRepository;
import org.example.laboratorGUI.repository.database.DBUserRepository;
import org.example.laboratorGUI.utils.GraphUtils;
import org.example.laboratorGUI.utils.Page;
import org.example.laboratorGUI.utils.PasswordUtils;
import org.example.laboratorGUI.utils.types.TipFriendship;
import org.example.laboratorGUI.utils.types.TipRata;
import org.example.laboratorGUI.utils.event.EntityChangeEvent;
import org.example.laboratorGUI.utils.event.EntityChangeEventType;
import org.example.laboratorGUI.validators.DuckValidator;
import org.example.laboratorGUI.validators.PersonValidator;
import org.example.laboratorGUI.validators.Validator;
import org.example.laboratorGUI.observer.Observable;

import java.time.LocalDate;
import java.util.*;

public class UserService extends Observable{
    private final DBUserRepository userRepo;
    private final DBFriendshipRepository friendshipRepo;
    private final DBFlockRepository flockRepo;

    private final int userPageSize = 6;
    private final int friendsPageSize = 4;

    private final Validator<Person> personValidator = new PersonValidator();
    private final Validator<Duck> duckValidator = new DuckValidator();

    public UserService(DBUserRepository userRepo, DBFriendshipRepository friendshipRepo, DBFlockRepository flockRepo) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
        this.flockRepo = flockRepo;
    }

    /**
     * Adds a new person to the network.
     *
     * @param userID the unique id of the person
     * @param username the username of the person
     * @param email the email of the person
     * @param password the password of the person
     * @param nume last name
     * @param prenume first name
     * @param dataNasterii birthdate
     * @param ocupatie occupation
     * @param nivelEmpatie empathy level
     * @throws ServiceException if the user already exists or validation fails
     */
    public void addPerson(Long userID, String username, String email, String password,
                          String nume, String prenume, LocalDate dataNasterii,
                          String ocupatie, Double nivelEmpatie) {
        String cpassword = PasswordUtils.hash(password);
        Person p = new Person(userID, username, email, cpassword, nume, prenume, ocupatie, dataNasterii, nivelEmpatie);
        personValidator.validate(p);
        userRepo.save(p);
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.USER_ADDED, p));
    }

    /**
     * Adds a new duck to the network.
     *
     * @param userID the unique id of the duck
     * @param username the username of the duck
     * @param email the email of the duck
     * @param password the password of the duck
     * @param tip the type of the duck
     * @param viteza speed of the duck
     * @param rezistenta endurance of the duck
     * @throws ServiceException if the user already exists or validation fails
     */
    public void addDuck(Long userID, String username, String email, String password,
                        TipRata tip, Double viteza, Double rezistenta, Long flockId) {
        Flock<? extends Duck> flock = flockRepo.findById(flockId);
        if (flock == null)
            throw new ServiceException("Flock not found");

        if (!flock.canAccept(tip))
            throw new ServiceException("Duck does not match flock type!");

        String cpassword = PasswordUtils.hash(password);
        Duck d = DuckFactory.createDuck(userID, username, email, cpassword, tip, viteza, rezistenta, flockId);
        duckValidator.validate(d);
        userRepo.save(d);

        if (tip == TipRata.FLYING)
            ((FlyingFlock) flock).addDuck((FlyingDuck) d);
        else if (tip == TipRata.SWIMMING)
            ((SwimmingFlock) flock).addDuck((SwimmingDuck) d);
        else
            ((FlyingSwimmingFlock) flock).addDuck((FlyingSwimmingDuck) d);
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.USER_ADDED, d));
    }

    /**
     * Updates the data of a person from the network. Empty arguments mean the old data will be kept
     *
     * @param userID the unique id of the person
     * @param password the password of the person
     * @param ocupatie occupation
     * @param nivelEmpatie empathy level
     * @throws ServiceException if the user doesn't exist
     */
    public void updatePerson(Long userID, String password, String ocupatie, Double nivelEmpatie) {
        User u = userRepo.findById(userID);
        if (u == null)
            throw new ServiceException("User not found");

        Person p = (Person) u;
        if (password != null && !password.isBlank()) {
            String cpassword = PasswordUtils.hash(password);
            p.setPassword(cpassword);
        }
        if (ocupatie != null && !ocupatie.isBlank())
            p.setJob(ocupatie);
        if (nivelEmpatie != null && nivelEmpatie >= 0 && nivelEmpatie <= 10)
            p.setEmpathy(nivelEmpatie);
        userRepo.update(p);
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.USER_UPDATED, p, u));
    }

    /**
     * Updates the data of a duck from the network. Empty arguments mean the old data will be kept
     *
     * @param userID the unique id of the duck
     * @param password the password of the duck
     * @param viteza speed of the duck
     * @param rezistenta endurance of the duck
     * @throws ServiceException if the user or flock doesn't exist
     */
    public void updateDuck(Long userID, String password, Double viteza, Double rezistenta, Long flockId) {
        User u = userRepo.findById(userID);
        if (u == null)
            throw new ServiceException("User not found");
        Duck d = (Duck) u;

        if (flockId != -1) {
            Flock<? extends Duck> newFlock = flockRepo.findById(flockId);
            if (newFlock == null)
                throw new ServiceException("Flock not found");
            if (newFlock.getType() == d.getType()) {
                Flock<? extends Duck> flock = flockRepo.findById(d.getFlockID());
                if (d.getType() == TipRata.FLYING){
                    ((FlyingFlock) flock).removeDuck((FlyingDuck) d);
                    ((FlyingFlock) newFlock).addDuck((FlyingDuck) d);
                }
                else if (d.getType() == TipRata.SWIMMING){
                    ((SwimmingFlock) flock).removeDuck((SwimmingDuck) d);
                    ((SwimmingFlock) newFlock).addDuck((SwimmingDuck) d);
                }
                else{
                    ((FlyingSwimmingFlock) flock).removeDuck((FlyingSwimmingDuck) d);
                    ((FlyingSwimmingFlock) newFlock).addDuck((FlyingSwimmingDuck) d);
                }
                d.setFlockID(flockId);
            }
        }
        if (password != null && !password.isBlank()) {
            String cpassword = PasswordUtils.hash(password);
            d.setPassword(cpassword);
        }
        if (viteza > 0)
            d.setSpeed(viteza);
        if (rezistenta > 0)
            d.setResistance(rezistenta);
        userRepo.update(d);
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.USER_UPDATED, d, u));
    }

    public void changePassword(Long userID, String password) {
        User u = userRepo.findById(userID);
        if (u == null)
            throw new ServiceException("User not found");
        String cpassword = PasswordUtils.hash(password);
        u.setPassword(cpassword);
        userRepo.update(u);
    }

    /**
     * Removes a user from the network, along with all their friendships.
     *
     * @param userID the id of the user to remove
     * @throws ServiceException if the user is not found
     */
    public void removeUser(Long userID) {
        User userTemp = userRepo.findById(userID);
        if (userTemp == null)
            throw new ServiceException("User not found");
        userRepo.delete(userID);
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.USER_REMOVED, userTemp));
    }

    public Page<User> getUserPage(int pageNumber){
        int nrUsers = getAllUsers().size();
        int totalPages = (int) Math.ceil((double) nrUsers / userPageSize);
        List<User> content = userRepo.findPage(pageNumber, userPageSize);

        return new Page<>(content, totalPages);
    }

    /**
     * Returns a list of all users in the network.
     *
     * @return list of all users
     */
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Page<Duck> getDuckPage(int pageNumber){
        int nrDucks = getAllDucks().size();
        int totalPages = (int) Math.ceil((double) nrDucks / userPageSize);
        List<Duck> content = userRepo.findDucksPage(pageNumber, userPageSize);

        return new Page<>(content, totalPages);
    }

    public Page<Duck> getDuckPageByType(int pageNumber, TipRata type) {
        int nrDucks = getAllDucksByType(type).size();
        int totalPages = (int) Math.ceil((double) nrDucks / userPageSize);
        List<Duck> content = userRepo.findDucksPageByType(pageNumber, userPageSize, type);

        return new Page<>(content, totalPages);
    }

    public List<Duck> getAllDucks() {
        return userRepo.findAllDucks();
    }

    public List<Duck> getAllDucksByType(TipRata type) {
        return userRepo.findDucksByType(type);
    }

    /**
     * Finds a user by ID.
     *
     * @param userID the ID of the user
     * @return the user if found, or null otherwise
     */
    public User getUserById(Long userID) {
        return userRepo.findById(userID);
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    /**
     * Adds a friendship between two users.
     *
     * @param sender the id of the first user
     * @param receiver the id of the second user
     * @throws ServiceException if users are the same, do not exist, or already friends
     */
    public void addFriend(Long sender, Long receiver) {
        if (Objects.equals(sender, receiver))
            throw new ServiceException("Cannot friend the same user");
        if (userRepo.findById(sender) == null || userRepo.findById(receiver) == null)
            throw new ServiceException("One of the users does not exist");
        long min = sender < receiver ? sender : receiver;
        long max = sender > receiver ? sender : receiver;
        Friendship fs = new Friendship(1L, min, max, TipFriendship.PENDING, receiver);
        friendshipRepo.save(fs);
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.FRIENDSHIP_SENT, fs));
    }

    public void acceptFriendRequest(Friendship friendship) {
        friendship.acceptRequest();
        friendshipRepo.updateStatus(friendship);
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.FRIENDSHIP_ACCEPTED, friendship));
    }

    public void rejectFriendRequest(Friendship friendship) {
        friendship.rejectRequest();
        friendshipRepo.updateStatus(friendship);
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.FRIENDSHIP_REJECTED, friendship));
    }

    /**
     * Removes the friendship between two users.
     *
     * @param userID1 the id of the first user
     * @param userID2 the id of the second user
     * @throws ServiceException if the friendship does not exist
     */
    public void removeFriend(Long userID1, Long userID2) {
        if (!friendshipRepo.findIfTwoUsersAreFriends(userID1, userID2))
            throw new ServiceException("Friendship does not exist");
        Friendship friendship = friendshipRepo.findByFriendship(userID1, userID2);
        friendshipRepo.delete(friendship.getFSID());
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.FRIENDSHIP_REMOVED, friendship));
    }

    /**
     * Returns a list of user IDs which are friends with the user ID given as argument
     *
     * @param userID the ID of the user
     * @return list of user IDs which are friends with the given user
     */
    public List<Long> getUsersFriends(Long userID){
        return friendshipRepo.findFriendsIds(userID);
    }

    public Page<String> getFriendsPage(int pageNumber, Long userID) {
        int nrFriends = getUsersFriends(userID).size();
        int totalPages = (int) Math.ceil((double) nrFriends / friendsPageSize);
        List<String> content = friendshipRepo.findFriendsPage(userID, pageNumber, friendsPageSize);

        return new Page<>(content, totalPages);
    }

    public List<Friendship> getPendingRequests(Long userID) {
        return friendshipRepo.findFriendRequests(userID, TipFriendship.PENDING);
    }

    public List<Friendship> getRejectedRequests(Long userID) {
        return friendshipRepo.findFriendRequests(userID, TipFriendship.REJECTED);
    }

    /**
     * Returns the number of connected communities in the network.
     *
     * @return the number of communities
     * @throws RepositoryException if no friendships exist
     */
    public int numberOfCommunities() {
        Map<Long, List<Long>> adj = friendshipRepo.findAdjacentIDs();
        Set<Long> visited = new HashSet<>();
        int count = 0;
        for (Long id : adj.keySet()) {
            if (!visited.contains(id)) {
                count++;
                GraphUtils.dfs(id, adj, visited);
            }
        }
        return count;
    }

    /**
     * Finds the most sociable community based on the largest diameter.
     *
     * @return a Map.Entry where the key is the list of user IDs in the community
     *         and the value is the diameter of the community
     * @throws ServiceException if no friendships exist
     */
    public Map.Entry<List<Long>, Integer> mostSociableCommunity() {
        Map<Long, List<Long>> adj = friendshipRepo.findAdjacentIDs();
        Set<Long> visited = new HashSet<>();
        List<Long> bestComponent = new ArrayList<>();
        int bestDiameter = -1;
        for (Long id : adj.keySet()) {
            if (!visited.contains(id)) {
                List<Long> comp = new ArrayList<>();
                GraphUtils.collectComponent(id, adj, comp, visited);
                int diam = GraphUtils.computeDiameter(comp, adj);
                if (diam > bestDiameter) {
                    bestDiameter = diam;
                    bestComponent = comp;
                }
            }
        }
        return Map.entry(bestComponent, Math.max(bestDiameter, 0));
    }
}
