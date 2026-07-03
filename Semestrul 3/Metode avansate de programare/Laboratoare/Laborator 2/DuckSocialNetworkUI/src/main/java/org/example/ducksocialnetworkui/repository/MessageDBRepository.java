package org.example.ducksocialnetworkui.repository;

import org.example.ducksocialnetworkui.domain.Message;
import org.example.ducksocialnetworkui.exception.RepositoryException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageDBRepository implements MessageRepository {
    private final String url;
    private final String username;
    private final String password;

    public MessageDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Optional<Message> findOne(Connection connection,Long id) throws SQLException{
        String sql = "SELECT * FROM messages WHERE id = ?";
        try(PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setLong(1,id);
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                Message message=createMessage(connection,id,resultSet);
                loadReceivers(message);
                return Optional.of(message);
            }
        }
        return Optional.empty();
    }

    private void loadReceivers(Message message) throws SQLException {
        String sql = "SELECT to_id FROM users_messages WHERE message_id = ?";
        Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, message.getId());
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            long u = rs.getLong("to_id");
            message.addTo(u);
        }
    }

    private void saveReceivers(Message message) throws SQLException {
        String sql = "INSERT INTO users_messages (to_id, message_id) VALUES (?, ?)";
        Connection connection = DriverManager.getConnection(url,username,password);
        PreparedStatement statement = connection.prepareStatement(sql);

        for (long u : message.getTo()) {
            statement.setLong(1, u);
            statement.setLong(2, message.getId());
            statement.addBatch();
        }

        statement.executeBatch();
    }

    private Message createMessage(Connection connection, Long id, ResultSet resultSet) throws SQLException {
        Long from = resultSet.getLong("from_id");

        String text = resultSet.getString("message");

        LocalDateTime date = resultSet.getTimestamp("data").toLocalDateTime();

        Message message = new Message(id,from,text,date);

        Long reply = resultSet.getLong("reply");

        if (!resultSet.wasNull()) {
            Optional<Message> replyMessage = this.findOne(connection,reply);
            replyMessage.ifPresent(message::setReply);
        }
        return message;
    }


    @Override
    public Optional<Message> findOne(Long id) {

        try(Connection connection=DriverManager.getConnection(url,username,password)){
            return findOne(connection,id);
        }catch (SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Iterable<Message> findAll() {
        List<Message> messages = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement("SELECT * FROM messages");
            ResultSet resultSet=statement.executeQuery()){

            while(resultSet.next()){
                Message message=createMessage(connection,resultSet.getLong("id"),resultSet);
                loadReceivers(message);
                messages.add(message);
            }

            return messages;

        } catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Optional<Message> save(Message message) {
        String sql = "INSERT INTO messages (from_id,message,data,reply) VALUES (?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement=connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){

            statement.setLong(1,message.getFrom());
            statement.setString(2, message.getMessage());
            statement.setDate(3, Date.valueOf(message.getData().toLocalDate()));
            if (message.getReply() != null)
                statement.setLong(4, message.getReply().getId());
            else
                statement.setNull(4, Types.BIGINT);

            int response = statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next())
                message.setId(rs.getLong(1));

            if(response != 0) {
                saveReceivers(message);
                return Optional.of(message);
            }
            return Optional.empty();

        }catch (SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Optional<Message> delete(Long id) {
        String sql = "DELETE FROM messages WHERE id = ?";
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement(sql)){

            var message=findOne(id);
            if(message.isEmpty())
                return Optional.empty();

            statement.setLong(1,id);
            int response = statement.executeUpdate();

            return response!=0 ? message : Optional.empty();

        }catch (SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<Message> getMessagesBetweenUsers(Long user1, Long user2) {

        List<Message> messages = new ArrayList<>();

        String sql = """
                SELECT m.* FROM messages m
                JOIN users_messages um ON um.message_id = m.id
                WHERE (m.from_id = ? AND um.to_id = ?) OR (m.from_id = ? AND um.to_id = ?)
                ORDER BY m.data;
                """;
        try (Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, user1);
            statement.setLong(2, user2);
            statement.setLong(3, user2);
            statement.setLong(4, user1);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Message message = createMessage(connection, rs.getLong("id"), rs);
                loadReceivers(message);
                messages.add(message);
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return messages;
    }
}
