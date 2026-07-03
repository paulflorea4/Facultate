package org.example.repos;

import lombok.RequiredArgsConstructor;
import org.example.models.User;
import org.example.repos.dtos.Page;
import org.example.repos.dtos.Pageable;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
public class UserDbRepository implements UserRepository {
    private final String url;
    private final String username;
    private final String password;

    @Override
    public Optional<User> findById(Integer id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
            statement.setInt(1, id);
            var res = statement.executeQuery();

            if (res.next()) {
                return Optional.of(createUser(id, res));
            }

            return Optional.empty();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static User createUser(Integer id, ResultSet res) throws SQLException {
        String username = res.getString("username");
        var create_d = getDateColumn(res, "create_date");
        var modify_d = getDateColumn(res, "modify_date");
        int credits = res.getInt("credit");

        var user = new User(username, create_d, modify_d, credits);
        user.setId(id);
        return user;
    }

    private static LocalDateTime getDateColumn(ResultSet res, String columnName) throws SQLException {
        var time = res.getTimestamp(columnName);
        return time == null ? null : time.toLocalDateTime();
    }

    @Override
    public Iterable<User> getAll() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("SELECT * FROM users");
            var res = statement.executeQuery();

            Set<User> users = new LinkedHashSet<>();

            while (res.next()) {
                users.add(createUser(res.getInt("id"), res));
            }

            return users;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> save(User user) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("INSERT INTO users (username, create_date, modify_date, credit) VALUES (?, ?, ?, ?)");
            statement.setString(1, user.getUsername());
            statement.setTimestamp(2, user.getCreateDate() == null ? null : Timestamp.valueOf(user.getCreateDate()));
            statement.setTimestamp(3, user.getModifyDate() == null ? null : Timestamp.valueOf(user.getModifyDate()));
            statement.setInt(4, user.getCredits());

            var res = statement.executeUpdate();
            return res != 0 ? Optional.of(user) : Optional.empty();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> update(User user) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("UPDATE users SET username = ?, create_date = ?, modify_date = ?, credit = ? WHERE id = ?");
            statement.setString(1, user.getUsername());
            statement.setTimestamp(2, user.getCreateDate() == null ? null : Timestamp.valueOf(user.getCreateDate()));
            statement.setTimestamp(3, user.getModifyDate() == null ? null : Timestamp.valueOf(user.getModifyDate()));
            statement.setInt(4, user.getCredits());
            statement.setInt(5, user.getId());

            var res = statement.executeUpdate();
            return res != 0 ? Optional.of(user) : Optional.empty();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> delete(Integer id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var user = findById(id);
            if (user.isEmpty())
                return Optional.empty();

            var statement = connection.prepareStatement("DELETE FROM users WHERE id = ?");
            statement.setInt(1, id);

            var res = statement.executeUpdate();
            return res != 0 ? user : Optional.empty();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int count(Connection connection) throws SQLException {
        var statement = connection.prepareStatement("SELECT COUNT(*) AS count FROM users");
        var res = statement.executeQuery();

        return res.next() ? res.getInt("count") : 0;
    }

    @Override
    public Page<User> getAllPage(Pageable page) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            int currentCount = count(connection);

            List<User> users = currentCount > 0 ? getAllPage(connection, page) : Collections.emptyList();

            return new Page<>(users, currentCount);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<User> getAllPage(Connection connection, Pageable page) throws SQLException {
        var statement = connection.prepareStatement("SELECT * FROM users OFFSET ? LIMIT ?");
        statement.setInt(1, page.getPageNumber() * page.getPageSize());
        statement.setInt(2, page.getPageSize());
        var res = statement.executeQuery();

        Set<User> users = new LinkedHashSet<>();

        while (res.next()) {
            users.add(createUser(res.getInt("id"), res));
        }

        return users.stream().toList();
    }
}
