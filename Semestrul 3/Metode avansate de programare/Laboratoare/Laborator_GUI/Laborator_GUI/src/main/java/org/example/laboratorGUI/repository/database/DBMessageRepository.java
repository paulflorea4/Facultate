package org.example.laboratorGUI.repository.database;

import org.example.laboratorGUI.domain.message.Message;
import org.example.laboratorGUI.exceptions.RepositoryException;
import org.example.laboratorGUI.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DBMessageRepository implements Repository<Long, Message> {
    private final Connection connection;

    public DBMessageRepository(Connection connection) {
        this.connection = connection;
    }

    private Message mapMessage(ResultSet rs) throws SQLException {
        Long messageID = rs.getLong("messageID");
        Long from = rs.getLong("fromID");
        String text = rs.getString("message");
        LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();
        Message msg = new Message(messageID, from, text, date);

        Long replyID = rs.getLong("replyTo");
        if (!rs.wasNull())
            msg.replyTo(findById(replyID));
        return msg;
    }

    @Override
    public Message findById(Long messageID) {
        String query = "SELECT * FROM Messages WHERE messageID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, messageID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Message msg = mapMessage(rs);
                findReceivers(msg);
                return msg;
            }
            return null;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find message:\n" + exception.getMessage());
        }
    }

    private void findReceivers(Message msg) throws SQLException {
        String q = "SELECT toID FROM MessagesTo WHERE messageID = ?";
        PreparedStatement ps = connection.prepareStatement(q);
        ps.setLong(1, msg.getMessageID());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            long userID = rs.getLong("toID");
            msg.addTo(userID);
        }
    }

    @Override
    public void save(Message msg) {
        String query = "INSERT INTO Messages(fromID, message, date, replyTo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, msg.getFrom());
            stmt.setString(2, msg.getMessage());
            stmt.setTimestamp(3, Timestamp.valueOf(msg.getDate()));
            if (msg.getReply() != null)
                stmt.setLong(4, msg.getReply().getMessageID());
            else
                stmt.setNull(4, Types.BIGINT);

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                long generatedID = rs.getLong(1);
                msg.setId(generatedID);
            }
            saveReceivers(msg);
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save message:\n" + exception.getMessage());
        }
    }

    private void saveReceivers(Message msg) throws SQLException {
        String q = "INSERT INTO MessagesTo(messageID, toID) VALUES (?, ?)";
        PreparedStatement ps = connection.prepareStatement(q);
        for (long u : msg.getTo()) {
            ps.setLong(1, msg.getMessageID());
            ps.setLong(2, u);
            ps.addBatch();
        }
        ps.executeBatch();
    }

    public List<Message> findConversation(long userA, long userB) {
        String query = """
        SELECT M.* FROM Messages M
        JOIN MessagesTo MT ON MT.messageID = M.messageID
        WHERE (M.fromID = ? AND MT.toID = ?) OR (M.fromID = ? AND MT.toID = ?)
        ORDER BY M.date;
        """;
        List<Message> messages = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, userA);
            ps.setLong(2, userB);
            ps.setLong(3, userB);
            ps.setLong(4, userA);

            ResultSet rs = ps.executeQuery();
            while (rs.next())
                messages.add(mapMessage(rs));
            return messages;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find conversation:\n" + exception.getMessage());
        }
    }

    @Override
    public Iterable<Message> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Long messageID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}

