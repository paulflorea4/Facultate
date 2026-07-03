package transport.persistence.repository.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import transport.model.Trip;
import transport.persistence.repository.TripRepository;
import transport.persistence.repository.utils.JdbcUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TripDbRepository implements TripRepository {

    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public TripDbRepository(Properties properties) {
        logger.info("Initializing TripDbRepository with properties: {}", properties);
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public Long add(Trip entity) {
        logger.info("Adding trip: {}", entity);

        String sql = "INSERT INTO trips (destination, date, hour, available_seats) VALUES (?, ?, ?, ?)";

        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entity.getDestination());
            statement.setString(2, entity.getDate());
            statement.setString(3, entity.getHour());
            statement.setInt(4, entity.getAvailableSeats());

            int result = statement.executeUpdate();
            logger.trace("Inserted rows: {}", result);

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    long id = keys.getLong(1);
                    entity.setId(id);
                    return id;
                }
            }

        } catch (SQLException e) {
            logger.error("Error adding trip", e);
            System.out.println("Error adding trip: " + e);
        }

        return null;
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting trip with id: {}", id);

        String sql = "DELETE FROM trips WHERE id = ?";

        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error deleting trip", e);
            System.out.println("Error deleting trip: " + e);
        }
    }

    @Override
    public void update(Trip entity) {
        logger.info("Updating trip: {}", entity);

        String sql = "UPDATE trips SET destination = ?, date = ?, hour = ?, available_seats = ? WHERE id = ?";

        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getDestination());
            statement.setString(2, entity.getDate());
            statement.setString(3, entity.getHour());
            statement.setInt(4, entity.getAvailableSeats());
            statement.setLong(5, entity.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error updating trip", e);
            System.out.println("Error updating trip: " + e);
        }
    }

    @Override
    public Trip findById(Long id) {
        logger.info("Finding trip by id: {}", id);

        String sql = "SELECT * FROM trips WHERE id = ?";

        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return extractTrip(rs);
                }
            }

        } catch (SQLException e) {
            logger.error("Error finding trip", e);
            System.out.println("Error finding trip: " + e);
        }

        return null;
    }

    @Override
    public List<Trip> findAll() {
        logger.info("Finding all trips");

        List<Trip> trips = new ArrayList<>();
        String sql = "SELECT * FROM trips";

        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                trips.add(extractTrip(rs));
            }

        } catch (SQLException e) {
            logger.error("Error finding trips", e);
            System.out.println("Error finding trips: " + e);
        }

        return trips;
    }

    @Override
    public List<Trip> findTripsByDestinationAndDepartureDate(String destination, String date, String hour) {
        logger.info("Finding trips by destination: {} , date: {} and hour: {}", destination, date, hour);

        List<Trip> trips = new ArrayList<>();

        String sql = """
                SELECT * FROM trips
                WHERE destination = ?
                AND date = ?
                AND hour = ?
                """;

        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, destination);
            statement.setString(2, date);
            statement.setString(3, hour);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    trips.add(extractTrip(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("Error finding filtered trips", e);
            System.out.println("Error finding trips: " + e);
        }

        return trips;
    }

    private Trip extractTrip(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String destination = rs.getString("destination");
        String date = rs.getString("date");
        String hour = rs.getString("hour");
        int availableSeats = rs.getInt("available_seats");

        return new Trip(id, destination, date, hour, availableSeats);
    }
}