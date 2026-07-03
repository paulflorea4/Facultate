package org.example.ducksocialnetworkui.repository;

import org.example.ducksocialnetworkui.domain.Duck;
import org.example.ducksocialnetworkui.domain.Friendship;
import org.example.ducksocialnetworkui.domain.FriendshipId;
import org.example.ducksocialnetworkui.domain.User;
import org.example.ducksocialnetworkui.dto.Dto;
import org.example.ducksocialnetworkui.dto.FriendshipDto;
import org.example.ducksocialnetworkui.exception.RepositoryException;
import org.example.ducksocialnetworkui.flock.Flock;
import org.example.ducksocialnetworkui.utils.paging.Page;
import org.example.ducksocialnetworkui.utils.paging.Pageable;

import java.sql.*;
import java.util.*;

import static org.example.ducksocialnetworkui.repository.UserDBRepository.createUser;

public class FriendshipDBRepository implements FriendshipRepository{
    private final String url;
    private final String username;
    private final String password;

    public FriendshipDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private FriendshipId normalize(FriendshipId id) {
        Long a = id.getUserId1();
        Long b = id.getUserId2();
        return new FriendshipId(Math.min(a, b), Math.max(a, b));
    }

    @Override
    public Optional<Friendship> findOne(FriendshipId id) {
        id=normalize(id);

        String sql = "SELECT * FROM friendships WHERE user_id1 = ? AND user_id2 = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id.getUserId1());
            statement.setLong(2, id.getUserId2());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(new Friendship(id.getUserId1(), id.getUserId2()));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Iterable<Friendship> findAll() {
        List<Friendship> list = new ArrayList<>();
        String sql = "SELECT * FROM friendships";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                long a = rs.getLong("user_id1");
                long b = rs.getLong("user_id2");
                list.add(new Friendship(a, b));
            }
            return list;

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Optional<Friendship> save(Friendship friendship) {
        String query = "INSERT INTO friendships(user_id1,user_id2) VALUES(?,?)";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement=connection.prepareStatement(query)){
            statement.setLong(1, friendship.getUserId1());
            statement.setLong(2, friendship.getUserId2());

            int response = statement.executeUpdate();

            return response!=0 ?  Optional.of(friendship) : Optional.empty();

        }catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Optional<Friendship> delete(FriendshipId id) {
        id = normalize(id);

        Optional<Friendship> existing = findOne(id);
        if (existing.isEmpty())
            return Optional.empty();

        String sql = "DELETE FROM friendships WHERE user_id1 = ? AND user_id2 = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id.getUserId1());
            statement.setLong(2, id.getUserId2());

            int response = statement.executeUpdate();

            return response == 0 ? Optional.empty() : existing;

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    private List<Friendship> getAllPage(Connection connection, Pageable page) throws SQLException{
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM friendships OFFSET ? LIMIT ?");
        statement.setLong(1, (long) page.getPageNumber() *page.getPageSize());
        statement.setLong(2,page.getPageSize());

        ResultSet resultSet=statement.executeQuery();

        Set<Friendship> friendships=new LinkedHashSet<>();

        while (resultSet.next()){

            friendships.add(new Friendship(resultSet.getLong("user_id1"),resultSet.getLong("user_id2")));
        }

        return friendships.stream().toList();
    }

    @Override
    public Page<Friendship> getAllPage(Pageable page) {
        try(Connection connection=DriverManager.getConnection(url,username,password)){
            int currenCount = count(connection);

            List<Friendship> friendships=currenCount > 0 ? getAllPage(connection,page) : Collections.emptyList();

            return new Page<>(friendships,currenCount);

        }catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    private int count(Connection connection) throws SQLException {
        PreparedStatement statement=connection.prepareStatement("SELECT COUNT(*) AS count FROM friendships");
        ResultSet resultSet=statement.executeQuery();

        return resultSet.next() ? resultSet.getInt("count") : 0;
    }

    @Override
    public Page<Friendship> getAllPage(Pageable page, Dto filter) {
        return getAllPage(page);
    }
}
