package org.example.laboratorGUI.repository.database;

import org.example.laboratorGUI.domain.user.duck.Duck;
import org.example.laboratorGUI.domain.user.person.Person;
import org.example.laboratorGUI.domain.user.User;
import org.example.laboratorGUI.exceptions.RepositoryException;
import org.example.laboratorGUI.factory.DuckFactory;
import org.example.laboratorGUI.repository.PagingRepository;
import org.example.laboratorGUI.utils.types.TipRata;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBUserRepository implements PagingRepository<Long, User> {
    private final Connection connection;

    public DBUserRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Maps the current row of the given ResultSet to a concrete user entity
     *
     * @param rs the ResultSet positioned on the row to be converted
     * @return the mapped user entity as TElem
     * @throws SQLException if it encountered a database error
     * @throws RepositoryException if the user type is not supported
     */
    private User mapUser(ResultSet rs) throws SQLException {
        String type = rs.getString("userType");
        return switch (type) {
            case "Person" -> findPerson(rs);
            case "Duck" -> findDuck(rs);
            default -> throw new RepositoryException("Unsupported user type!");
        };
    }

    /**
     * Finds a user by its ID.
     *
     * @param id the ID of the user to find
     * @return the user with the given ID, or null if it does not exist
     * @throws RepositoryException if the user type is unsupported or it encountered a database error
     */
    @Override
    public User findById(Long id) {
        String query = """
        SELECT *, U.type AS userType, D.type AS duckType
        FROM Users U
        LEFT JOIN People P ON P.userID = U.userID
        LEFT JOIN Ducks D ON D.userID = U.userID
        WHERE U.userID = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return mapUser(rs);
            return null;
        } catch (SQLException exception){
            throw new RepositoryException("Could not find user:\n" + exception.getMessage());
        }
    }

    public User findByUsername(String username) {
        String query = """
        SELECT * , U.type AS userType, D.type AS duckType
        FROM Users U
        LEFT JOIN People P ON P.userID = U.userID
        LEFT JOIN Ducks D ON D.userID = U.userID
        WHERE username = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return mapUser(rs);
            return null;
        } catch (SQLException exception){
            throw new RepositoryException("Could not find user:\n" + exception.getMessage());
        }
    }

    /**
     * Returns a list of all users stored in the database.
     *
     * @return a list containing all entities
     * @throws RepositoryException if the user type is unsupported, or it encountered a database error
     */
    @Override
    public List<User> findAll() {
        String query = """
        SELECT *, U.type AS userType, D.type AS duckType
        FROM Users U
        LEFT JOIN People P ON P.userID = U.userID
        LEFT JOIN Ducks D ON D.userID = U.userID
        """;
        List<User> users = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next())
                users.add(mapUser(rs));
            return users;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find all users:\n" + exception.getMessage());
        }
    }

    public List<Duck> findAllDucks() {
        String query = """
        SELECT *, U.type AS userType , D.type AS duckType
        FROM Users U
        JOIN Ducks D ON D.userID = U.userID
        """;
        List<Duck> ducks = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next())
                ducks.add((Duck) mapUser(rs));
            return ducks;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find all ducks:\n" + exception.getMessage());
        }
    }

    public List<Duck> findDucksByType(TipRata type) {
        String query = """
        SELECT *, U.type AS userType , D.type AS duckType
        FROM Users U
        JOIN Ducks D ON D.userID = U.userID
        WHERE D.type = ?
        """;
        List<Duck> ducks = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, type.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                ducks.add((Duck) mapUser(rs));
            return ducks;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find ducks by type:\n" + exception.getMessage());
        }
    }

    @Override
    public List<User> findPage(int pageNumber, int pageSize){
        String query = """
        SELECT *, U.type AS userType, D.type AS duckType
        FROM Users U
        LEFT JOIN People P ON P.userID = U.userID
        LEFT JOIN Ducks D ON D.userID = U.userID
        LIMIT ? OFFSET ?""";
        List<User> users = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setInt(1, pageSize);
            stmt.setInt(2, pageNumber * pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                users.add(mapUser(rs));
            return users;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find user page:\n" + exception.getMessage());
        }
    }

    public List<Duck> findDucksPage(int pageNumber, int pageSize){
        List<Duck> ducks = new ArrayList<>();
        String query = """
        SELECT *, U.type AS userType, D.type AS duckType
        FROM Users U 
        JOIN Ducks D ON U.userID = D.userID
        LIMIT ? OFFSET ?""";
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setInt(1, pageSize);
            stmt.setInt(2, pageNumber * pageSize);

            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                ducks.add((Duck) mapUser(rs));
            return ducks;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find duck page:\n" + exception.getMessage());
        }
    }

    public List<Duck> findDucksPageByType(int pageNumber, int pageSize, TipRata type){
        List<Duck> ducks = new ArrayList<>();
        String query = """
        SELECT *, U.type AS userType, D.type AS duckType
        FROM Users U
        JOIN Ducks D ON U.userID = D.userID
        WHERE D.type = ?
        LIMIT ? OFFSET ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setString(1, type.toString());
            stmt.setInt(2, pageSize);
            stmt.setInt(3, pageNumber * pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                ducks.add((Duck) mapUser(rs));
            return ducks;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find duck page:\n" + exception.getMessage());
        }
    }

    /**
     * Loads person's data from the database
     *
     * @param rs the ResultSet positioned on the row to be converted
     * @return the person with the loaded details
     * @throws RepositoryException if it encountered a database error
     */
    private Person findPerson(ResultSet rs) throws SQLException{
        Long userID = rs.getLong("userID");
        String username = rs.getString("username");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String surname = rs.getString("surname");
        String name = rs.getString("name");
        String job = rs.getString("job");
        Date dob = rs.getDate("dob");
        Double empathy = rs.getDouble("empathy");
        return new Person(userID, username, email, password, surname, name, job, dob.toLocalDate(), empathy);
    }

    /**
     * Loads duck's data from the database
     *
     * @param rs the ResultSet positioned on the row to be converted
     * @return the duck with the loaded details
     * @throws RepositoryException if it encountered a database error
     */
    private Duck findDuck(ResultSet rs) throws SQLException {
        Long userID = rs.getLong("userID");
        String username = rs.getString("username");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String duckType = rs.getString("duckType");
        Double speed = rs.getDouble("speed");
        Double resistance = rs.getDouble("resistance");
        Long flockID = rs.getLong("flockID");
        return DuckFactory.createDuck(userID, username, email, password, TipRata.valueOf(duckType), speed, resistance, flockID);
    }

    private String findUserType(User user) {
        return switch (user) {
            case Person ignored -> "Person";
            case Duck ignored -> "Duck";
            default -> throw new RepositoryException("Unsupported user type!");
        };
    }

    /**
     * Saves a new user in the database.
     * If a user with the same ID, username or email already exists, the entity is not added.
     *
     * @param user the user to save
     * @throws RepositoryException if the entity is null, or it encountered a database error
     */
    @Override
    public void save(User user) {
        String userQuery = "INSERT INTO Users (username, email, password, type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, findUserType(user));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                long generatedID = rs.getLong(1);
                user.setId(generatedID);
            }
            switch (user) {
                case Person person -> savePerson(person);
                case Duck duck -> saveDuck(duck);
                default -> throw new RepositoryException("Unsupported user type!");
            }
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save user:\n" + exception.getMessage());
        }
    }

    /**
     * Saves a new person in the database
     *
     * @param person the person to save
     * @throws RepositoryException if it encountered a database error
     */
    private void savePerson(Person person) {
        String personQuery = "INSERT INTO People (userID, surname, name, job, dob, empathy) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(personQuery)){
            stmt.setLong(1, person.getUserID());
            stmt.setString(2, person.getSurname());
            stmt.setString(3, person.getName());
            stmt.setString(4, person.getJob());
            stmt.setDate(5, Date.valueOf(person.getDob()));
            stmt.setDouble(6, person.getEmpathy());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save person:\n" + exception.getMessage());
        }
    }

    /**
     * Saves a new duck in the database
     *
     * @param duck the duck to save
     * @throws RepositoryException if encountered a database error
     */
    private void saveDuck(Duck duck) {
        String duckQuery = "INSERT INTO Ducks (userID, type, speed, resistance, flockID) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(duckQuery)){
            stmt.setLong(1, duck.getUserID());
            stmt.setString(2, duck.getType().toString());
            stmt.setDouble(3, duck.getSpeed());
            stmt.setDouble(4, duck.getResistance());
            stmt.setLong(5, duck.getFlockID());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save duck:\n" + exception.getMessage());
        }
    }

    /**
     * Deletes the user with the specified ID from the database.
     *
     * @param userID the ID of the entity to delete
     * @throws RepositoryException if the ID is null, the user type is unsupported or encountered a database error
     */
    @Override
    public void delete(Long userID) {
        String query = "DELETE FROM Users WHERE userID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, userID);
            stmt.executeUpdate();
        } catch (SQLException exception){
            throw new RepositoryException("Could not delete user:\n" + exception.getMessage());
        }
    }

    /**
     * Updates an existing user in the database
     *
     * @param user the entity to update
     * @throws RepositoryException if the entity is null, the user type is unsupported or encountered a database error
     */
    public void update(User user) {
        String query = "UPDATE Users SET password = ? WHERE userID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getPassword());
            stmt.setLong(2, user.getUserID());
            stmt.executeUpdate();
            switch (user) {
                case Person person -> updatePerson(person);
                case Duck duck -> updateDuck(duck);
                default -> throw new RepositoryException("Unsupported user type!");
            }
        } catch (SQLException exception){
            throw new RepositoryException("Could not update user:\n" + exception.getMessage());
        }
    }

    /**
     * Updates an existing person in the database
     *
     * @param person the person to update
     * @throws RepositoryException if encountered a database error
     */
    private void updatePerson(Person person) {
        String query = "UPDATE People SET job = ?, empathy = ? WHERE userID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setString(1, person.getJob());
            stmt.setDouble(2, person.getEmpathy());
            stmt.setLong(3, person.getUserID());
            stmt.executeUpdate();
        } catch (SQLException exception){
            throw new RepositoryException("Could not update person:\n" + exception.getMessage());
        }
    }

    /**
     * Updates an existing duck in the database
     *
     * @param duck the entity to update
     * @throws RepositoryException if encountered a database error
     */
    private void updateDuck(Duck duck) {
        String query = "UPDATE Ducks SET speed = ?, resistance = ?, flockID = ? WHERE userID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setDouble(1, duck.getSpeed());
            stmt.setDouble(2, duck.getResistance());
            stmt.setLong(3, duck.getFlockID());
            stmt.setLong(4, duck.getUserID());
            stmt.executeUpdate();
        } catch (SQLException exception){
            throw new RepositoryException("Could not update duck:\n" + exception.getMessage());
        }
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
