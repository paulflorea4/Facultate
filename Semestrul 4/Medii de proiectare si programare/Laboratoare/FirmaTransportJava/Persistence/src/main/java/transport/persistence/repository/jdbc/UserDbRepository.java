package transport.persistence.repository.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import transport.model.User;
import transport.persistence.repository.UserRepository;
import transport.persistence.repository.utils.JdbcUtils;
import transport.persistence.repository.utils.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserDbRepository implements UserRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public UserDbRepository(Properties properties) {
        logger.info("Initializind UserDbRepository with properties: {} ", properties);
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public User findByUsername(String username) {
        logger.info("Attempting login for user: {}", username);
        String sql = "SELECT * FROM users WHERE username = ? LIMIT 1    ";
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try(var result = statement.executeQuery()) {
                if(result.next()) {
                    String password = result.getString("password");
                    User user = new User(result.getLong("id"), username, password);
                    logger.trace("User found: {}", user);
                    return user;
                } else {
                    logger.trace("No user found with username: {}", username);
                    return null;
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error during finding by username: " + e);
            return null;
        }
    }

    @Override
    public Long add(User entity) {
        logger.info("Adding new user: {}", entity);
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, PasswordUtils.hash(entity.getPassword()));
            int result = statement.executeUpdate();
            logger.trace("User added successfully, result: {}", result);

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    entity.setId(id);
                    return id;
                }
            }
            return null;
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error adding user: " + e);
            return null;
        } finally {
            logger.traceExit();
        }
    }

    @Override
    public void delete(Long aLong) {
        logger.info("Deleting user: {}", aLong);
        String sql = "DELETE FROM users WHERE id = ?";
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, aLong);
            int result = statement.executeUpdate();
            logger.trace("User deleted successfully, result: {}", result);
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error deleting user: " + e);
        }
        logger.traceExit();
    }

    @Override
    public void update(User entity) {
        logger.info("Updating user: {}", entity);
        String sql = "UPDATE users SET username = ?, password = ? WHERE id = ?";
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, PasswordUtils.hash(entity.getPassword()));
            statement.setLong(3, entity.getId());
            int result = statement.executeUpdate();
            logger.trace("User updated successfully, result: {}", result);
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error updating user: " + e);
        }
        logger.traceExit();
    }

    @Override
    public User findById(Long aLong) {
        logger.info("Finding user: {}", aLong);
        String sql = "SELECT * FROM users WHERE id = ?";
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, aLong);
            try(var result = statement.executeQuery()) {
                if (result.next()) {
                    String username = result.getString("username");
                    String password = result.getString("password");
                    User user = new User(aLong, username, password);
                    logger.trace("User found: {}", user);
                    return user;
                } else {
                    logger.trace("User not found with id: {}", aLong);
                    return null;
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error finding user: " + e);
            return null;
        }
    }

    @Override
    public List<User> findAll() {
        logger.info("Finding all users");
        String sql = "SELECT * FROM users";
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            try(var result = statement.executeQuery()) {
                List<User> users = new ArrayList<>();
                while (result.next()) {
                    Long id = result.getLong("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    User user = new User(id, username, password);
                    users.add(user);
                    logger.trace("User found: {}", user);
                }
                return users;
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error finding users: " + e);
        }
        logger.traceExit();
        return new ArrayList<>();
    }
}
