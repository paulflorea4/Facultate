package org.example.laboratorGUI.repository.database;

import org.example.laboratorGUI.domain.flock.Flock;
import org.example.laboratorGUI.domain.user.duck.Duck;
import org.example.laboratorGUI.exceptions.RepositoryException;
import org.example.laboratorGUI.factory.FlockFactory;
import org.example.laboratorGUI.repository.Repository;
import org.example.laboratorGUI.utils.types.TipRata;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBFlockRepository implements Repository<Long, Flock<? extends Duck>> {
    private final Connection connection;

    public DBFlockRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Maps the current row of the ResultSet to a Flock entity.
     *
     * @param rs the ResultSet positioned on the row to map
     * @return the mapped Flock object
     * @throws SQLException if it encountered a database error
     */
    private Flock<? extends Duck> mapFlock(ResultSet rs) throws SQLException {
        Long flockID = rs.getLong("flockID");
        String name = rs.getString("name");
        String type = rs.getString("type");
        return FlockFactory.createFlock(flockID, name, TipRata.valueOf(type));
    }

    /**
     * Finds a flock by its ID.
     *
     * @param flockID the ID of the flock
     * @return the flock, or null if not found
     * @throws RepositoryException if it encountered a database error
     */
    public Flock<? extends Duck> findById(Long flockID) {
        String query = "SELECT * FROM Flocks WHERE flockID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, flockID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return mapFlock(rs);
            return null;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find flock:\n" + exception.getMessage());
        }
    }

    /**
     * Returns all flocks stored in the database.
     *
     * @return a list of all flocks
     * @throws RepositoryException if it encountered a database error
     */
    public List<Flock<? extends Duck>> findAll() {
        String query = "SELECT * FROM Flocks";
        List<Flock<? extends Duck>> flocks = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next())
                flocks.add(mapFlock(rs));
            return flocks;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find all flocks:\n" + exception.getMessage());
        }
    }

    /**
     * Saves a new flock in the database.
     *
     * @param flock the flock to save
     * @throws RepositoryException if it encountered a database error
     */
    public void save(Flock<? extends Duck> flock) {
        String query = "INSERT INTO Flocks (name, type) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, flock.getName());
            stmt.setString(2, flock.getType().toString());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save flock:\n" + exception.getMessage());
        }
    }

    /**
     * Deletes a flock from the database.
     *
     * @param flockID the ID of the flock to delete
     * @throws RepositoryException if it encountered a database error
     */
    public void delete(Long flockID) {
        String query = "DELETE FROM Flocks WHERE flockID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, flockID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not delete flock:\n" + exception.getMessage());
        }
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
