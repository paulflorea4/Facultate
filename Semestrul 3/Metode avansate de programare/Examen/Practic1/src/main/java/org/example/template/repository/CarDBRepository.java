package org.example.template.repository;

import org.example.template.database.DBConnection;
import org.example.template.domain.Car;
import org.example.template.domain.Placeholder;
import org.example.template.domain.Status;
import org.example.template.exceptions.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDBRepository implements Repository<Long, Car>{
    @Override
    public void save(Car entity) {

    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void update(Long aLong, Car entity) {

    }

    @Override
    public Car findById(Long aLong) {
        return null;
    }

    @Override
    public List<Car> findAll() {
        List<Car> cars = new ArrayList<>();

        String query = "SELECT * FROM cars";
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                cars.add(new Car(rs.getLong("id"),rs.getString("denumire"),rs.getString("descriere"),rs.getInt("pret"), Status.valueOf(rs.getString("status"))));
            }
            return cars;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not get placeholders:\n" + exception.getMessage());
        }
    }

    public void updateStatus(Long id,String status){
        String query = "UPDATE cars SET status = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not update placeholder:\n" + exception.getMessage());
        }
    }
}
