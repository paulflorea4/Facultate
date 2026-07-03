package org.example.template.repository;

import org.example.template.database.DBConnection;
import org.example.template.domain.Placeholder;
import org.example.template.exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBPagingRepository extends DBRepository implements PagingRepository<Integer, Placeholder> {
    public List<Placeholder> findPage(int pageNumber, int pageSize) {
        List<Placeholder> placeholders = new ArrayList<>();

        String query = """
        SELECT * FROM Placeholders
        LIMIT ? OFFSET ?
        """;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setInt(1, pageSize);
            stmt.setInt(2, pageNumber * pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                placeholders.add(new Placeholder(rs.getInt("ID"), rs.getString("placeholder")));
            return placeholders;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find page:\n" + exception.getMessage());
        }
    }
}
