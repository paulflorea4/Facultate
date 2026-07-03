package org.example.template.repository;

import org.example.template.database.DBConnection;
import org.example.template.domain.Placeholder;
import org.example.template.exceptions.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBRepository implements Repository<Integer, Placeholder>{
    public void save(Placeholder entity) {
        String query = "INSERT INTO Placeholders (placeholder) VALUES (?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, entity.getPlaceholder());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save placeholder:\n" + exception.getMessage());
        }
    }

    public void delete(Integer ID) {
        String query = "DELETE FROM Placeholders WHERE ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not delete placeholder:\n" + exception.getMessage());
        }
    }

    public void update(Integer ID, Placeholder entity) {
        String query = "UPDATE Placeholders SET placeholder = ? WHERE ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, entity.getPlaceholder());
            stmt.setInt(2, ID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not update placeholder:\n" + exception.getMessage());
        }
    }

    public Placeholder findById(Integer ID) {
        String query = "SELECT * FROM Placeholders WHERE ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Placeholder(rs.getInt("ID"), rs.getString("placeholder"));
            }
            return null;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find placeholder:\n" + exception.getMessage());
        }
    }

    public List<Placeholder> findAll() {
        List<Placeholder> placeholders = new ArrayList<>();

        String query = "SELECT * FROM Placeholders";
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                placeholders.add(new Placeholder(rs.getInt("ID"), rs.getString("placeholder")));
            }
            return placeholders;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not get placeholders:\n" + exception.getMessage());
        }
    }
}
