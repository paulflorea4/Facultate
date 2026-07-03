package org.example.template.repository;

import org.example.template.database.DBConnection;
import org.example.template.domain.Placeholder;
import org.example.template.domain.Rol;
import org.example.template.domain.User;
import org.example.template.exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDBRepository implements Repository<Long, User>{
    @Override
    public void save(User entity) {

    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void update(Long aLong, User entity) {

    }

    @Override
    public User findById(Long aLong) {
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        return null;
    }

    public User findByUsernameAndPassword(String username, String password){
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getLong("id"),rs.getString("username"), rs.getString("password"), Rol.valueOf(rs.getString("rol")));
            }
            return null;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find user:\n" + exception.getMessage());
        }
    }
}
