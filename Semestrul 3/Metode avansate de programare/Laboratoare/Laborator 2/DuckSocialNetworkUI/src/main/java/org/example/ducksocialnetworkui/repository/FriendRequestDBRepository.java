package org.example.ducksocialnetworkui.repository;

import org.example.ducksocialnetworkui.domain.FriendRequest;
import org.example.ducksocialnetworkui.domain.FriendRequestStatus;
import org.example.ducksocialnetworkui.exception.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendRequestDBRepository implements FriendRequestRepository{
    private final String url;
    private final String username;
    private final String password;

    public FriendRequestDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private FriendRequest create(ResultSet rs) throws SQLException {
        return new FriendRequest(
                rs.getLong("id"),
                rs.getLong("from_id"),
                rs.getLong("to_id"),
                FriendRequestStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("date").toLocalDateTime()
        );
    }

    @Override
    public Optional<FriendRequest> findByUsers(Long from, Long to) {
        String sql = """
            SELECT * FROM friend_requests
            WHERE from_id=? AND to_id=?
        """;
        try (Connection c = DriverManager.getConnection(url, username, password);
             PreparedStatement s = c.prepareStatement(sql)) {

            s.setLong(1, from);
            s.setLong(2, to);
            ResultSet rs = s.executeQuery();
            return rs.next() ? Optional.of(create(rs)) : Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<FriendRequest> findPendingForUser(Long userId) {
        String sql = """
            SELECT * FROM friend_requests
            WHERE to_id=? AND status='PENDING'
        """;
        List<FriendRequest> list = new ArrayList<>();
        try (Connection c = DriverManager.getConnection(url, username, password);
             PreparedStatement s = c.prepareStatement(sql)) {

            s.setLong(1, userId);
            ResultSet rs = s.executeQuery();
            while (rs.next())
                list.add(create(rs));
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return list;
    }

    @Override
    public Optional<FriendRequest> updateStatus(Long id, FriendRequestStatus status) {
        String sql = "UPDATE friend_requests SET status=? WHERE id=?";
        try (Connection c = DriverManager.getConnection(url, username, password);
             PreparedStatement s = c.prepareStatement(sql)) {

            s.setString(1, status.name());
            s.setLong(2, id);
            int updated = s.executeUpdate();
            if (updated == 0) return Optional.empty();

            PreparedStatement f = c.prepareStatement(
                    "SELECT * FROM friend_requests WHERE id=?");
            f.setLong(1, id);
            ResultSet rs = f.executeQuery();
            return rs.next() ? Optional.of(create(rs)) : Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Optional<FriendRequest> save(FriendRequest r) {
        String sql = """
            INSERT INTO friend_requests(from_id,to_id,status,date)
            VALUES(?,?,?,?) RETURNING id
        """;
        try (Connection c = DriverManager.getConnection(url, username, password);
             PreparedStatement s = c.prepareStatement(sql)) {

            s.setLong(1, r.getFromId());
            s.setLong(2, r.getToId());
            s.setString(3, r.getStatus().name());
            s.setTimestamp(4, Timestamp.valueOf(r.getDate()));

            ResultSet rs = s.executeQuery();
            if (rs.next())
                return Optional.of(new FriendRequest(
                        rs.getLong(1),
                        r.getFromId(),
                        r.getToId(),
                        r.getStatus(),
                        r.getDate()
                ));

            return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<FriendRequest> findAllForUser(Long userId) {
        String sql = """
        SELECT * FROM friend_requests
        WHERE to_id = ?
        ORDER BY date DESC
    """;

        List<FriendRequest> list = new ArrayList<>();

        try (Connection c = DriverManager.getConnection(url, username, password);
             PreparedStatement s = c.prepareStatement(sql)) {

            s.setLong(1, userId);
            ResultSet rs = s.executeQuery();

            while (rs.next()) {
                list.add(create(rs));
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return list;
    }

    @Override
    public Optional<FriendRequest> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<FriendRequest> findOne(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Iterable<FriendRequest> findAll() {
        return null;
    }
}
