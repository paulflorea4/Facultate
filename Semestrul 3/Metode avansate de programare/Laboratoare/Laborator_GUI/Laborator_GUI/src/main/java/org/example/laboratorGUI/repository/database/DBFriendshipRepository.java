package org.example.laboratorGUI.repository.database;

import org.example.laboratorGUI.domain.friendship.Friendship;
import org.example.laboratorGUI.exceptions.RepositoryException;
import org.example.laboratorGUI.repository.Repository;
import org.example.laboratorGUI.utils.types.TipFriendship;

import java.sql.*;
import java.util.*;

public class DBFriendshipRepository implements Repository<Long, Friendship> {
    private final Connection connection;

    public DBFriendshipRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Saves a new friendship between two users.
     * If a friendship already exists between the users, a RepositoryException is thrown.
     *
     * @param friendship the friendship between two users
     * @throws RepositoryException if the users are already friends
     */
    public void save(Friendship friendship) {
        String query = "INSERT INTO Friendships(userId1, userId2, status, receiverId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setLong(1, friendship.getUserId1());
            stmt.setLong(2, friendship.getUserId2());
            stmt.setString(3, friendship.getStatus().toString());
            stmt.setLong(4, friendship.getSentTo());
            stmt.executeUpdate();
        } catch (SQLException exception){
            throw new RepositoryException("Could not save friendship:\n" + exception.getMessage());
        }
    }

    public void updateStatus(Friendship friendship) {
        String query = """
        UPDATE Friendships
        SET status = ?
        WHERE userID1 = ? AND userID2 = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, friendship.getStatus().toString());
            stmt.setLong(2, friendship.getUserId1());
            stmt.setLong(3, friendship.getUserId2());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not update friendship status:\n" + exception.getMessage());
        }
    }

    /**
     * Deletes the friendship between two users.
     * If either user ID does not exist in the repository, a RepositoryException is thrown.
     *
     * @param friendshipId the id of the friendship
     * @throws RepositoryException if one of the user IDs is invalid
     */
    public void delete(Long friendshipId) {
        String query = "DELETE FROM Friendships WHERE friendshipID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setLong(1, friendshipId);
            int affected = stmt.executeUpdate();

            if (affected != 1)
                throw new RepositoryException("The friendship does not exist!");
        } catch (SQLException exception){
            throw new RepositoryException("Could not delete friendship:\n" + exception.getMessage());
        }
    }

    public void delete(Friendship friendship) {
        String query = """
        DELETE FROM Friendships
        WHERE userID1 = ? AND userID2 = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setLong(1, friendship.getUserId1());
            stmt.setLong(2, friendship.getUserId2());
            int affected = stmt.executeUpdate();

            if (affected != 1)
                throw new RepositoryException("The friendship does not exist!");
        } catch (SQLException exception){
            throw new RepositoryException("Could not delete friendship:\n" + exception.getMessage());
        }
    }

    /**
     * Returns a list of user IDs that are friends with the given user.
     * If the user has no friends, returns an empty list.
     *
     * @param userId the id of the user
     * @return a list of user IDs who are friends with the given user
     */
    public List<Long> findFriendsIds(Long userId) {
        String query = """
        SELECT
            CASE
                WHEN userID1 = ? THEN userID2
                ELSE userID1
            END AS friendID
        FROM Friendships
        WHERE (userID1 = ? OR userID2 = ?) AND status = 'ACCEPTED'
        """;

        List<Long> ids = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, userId);
            stmt.setLong(3, userId);

            var rs = stmt.executeQuery();
            while (rs.next())
                ids.add(rs.getLong("friendID"));
            return ids;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find the ids of the friends:\n" + exception.getMessage());
        }
    }

    private Friendship mapFriendship(ResultSet rs) throws SQLException {
        Long fsID = rs.getLong(1);
        Long userId1 = rs.getLong(2);
        Long userId2 = rs.getLong(3);
        TipFriendship status = TipFriendship.valueOf(rs.getString(4));
        Long senderId = rs.getLong(5);
        return new Friendship(fsID, userId1, userId2, status, senderId);
    }

    public List<Friendship> findFriendRequests(Long userId, TipFriendship status) {
        String query = """
        SELECT * FROM Friendships
        WHERE (userID1 = ? OR userID2 = ?) AND status = ?
        """;

        List<Friendship> friendships = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, userId);
            stmt.setString(3, status.toString());

            var rs = stmt.executeQuery();
            while (rs.next())
                friendships.add(mapFriendship(rs));
            return friendships;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find friend requests:\n" + exception.getMessage());
        }
    }

    /**
     * Returns a paginated list of friends for the given user.
     *
     * @param userId the id of the user
     * @param pageNumber 0-based page index
     * @param pageSize number of items per page
     * @return a list of usernames
     */
    public List<String> findFriendsPage(Long userId, int pageNumber, int pageSize) {
        String query = """
        SELECT u.username
        FROM Friendships f
        JOIN Users u ON (u.userID = CASE WHEN f.userID1 = ? THEN f.userID2 ELSE f.userID1 END)
        WHERE (f.userID1 = ? OR f.userID2 = ?) AND f.status = 'ACCEPTED'
        LIMIT ? OFFSET ?
        """;

        List<String> usernames = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, userId);
            stmt.setLong(3, userId);
            stmt.setInt(4, pageSize);
            stmt.setInt(5, pageNumber * pageSize);

            var rs = stmt.executeQuery();
            while (rs.next())
                usernames.add(rs.getString("username"));
            return usernames;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find friends page:\n" + exception.getMessage());
        }
    }

    public Friendship findById(Long friendshipId) {
        String query = "SELECT * FROM Friendships WHERE friendshipID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setLong(1, friendshipId);
            var rs = stmt.executeQuery();
            if (rs.next())
                return mapFriendship(rs);
            return null;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find friendship:\n" + exception.getMessage());
        }
    }

    public Friendship findByFriendship(Long userId1, Long userId2) {
        String query = "SELECT * FROM Friendships WHERE userID1 = ? AND userID2 = ?";
        long min = userId1 < userId2 ? userId1 : userId2;
        long max = userId1 > userId2 ? userId1 : userId2;
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setLong(1, min);
            stmt.setLong(2, max);
            var rs = stmt.executeQuery();
            if (rs.next())
                return mapFriendship(rs);
            return null;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find friendship:\n" + exception.getMessage());
        }
    }

    public List<Friendship> findAll() {
        String query = "SELECT * FROM Friendships";
        List<Friendship> friendships = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            var rs = stmt.executeQuery();
            while (rs.next())
                friendships.add(mapFriendship(rs));
            return friendships;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find all friendships:\n" + exception.getMessage());
        }
    }

    public Map<Long, List<Long>> findAdjacentIDs() {
        String query = """
        WITH AllRelationships AS (
            SELECT userID1 AS userID, userID2 AS friendID
            FROM Friendships
       \s
            UNION ALL
       \s
            SELECT userID2 AS userID, userID1 AS friendID
            FROM Friendships
        )
        SELECT userID, ARRAY_AGG(friendID ORDER BY friendID ASC) AS friendsList
        FROM AllRelationships
        GROUP BY userID
        ORDER BY userID
       """;
        Map<Long, List<Long>> adj = new HashMap<>();
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()){
                Long userID = rs.getLong("userID");
                List<Long> friends = Arrays.stream(((Long[]) rs.getArray("friendsList").getArray())).toList();
                adj.put(userID, friends);
            }
            return adj;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find adjacent IDs:\n" + exception.getMessage());
        }
    }

    /**
     * Checks if two users are friends.
     *
     * @param userId1 the id of the first user
     * @param userId2 the id of the second user
     * @return true if the users are friends, false otherwise
     */
    public boolean findIfTwoUsersAreFriends(Long userId1, Long userId2){
        String query = """
        SELECT userID1, userID2 FROM Friendships
        WHERE (userId1 = ? AND userId2 = ?)
        """;
        Long min  = userId1 < userId2 ? userId1 : userId2;
        Long max = userId1 > userId2 ? userId1 : userId2;

        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setLong(1, min);
            stmt.setLong(2, max);
            var rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException exception){
            throw new RepositoryException("Could not find if they are friends:\n" + exception.getMessage());
        }
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
