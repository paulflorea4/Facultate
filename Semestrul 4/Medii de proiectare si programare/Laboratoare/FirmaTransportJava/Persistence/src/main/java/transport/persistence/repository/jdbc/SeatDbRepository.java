package transport.persistence.repository.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import transport.model.Seat;
import transport.persistence.repository.SeatRepository;
import transport.persistence.repository.utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SeatDbRepository implements SeatRepository {

    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger(SeatDbRepository.class);

    public SeatDbRepository(Properties properties) {
        logger.info("Initializing SeatDbRepository with properties: {}", properties);
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public List<Seat> findSeatsForTrip(Long tripId) {
        logger.info("Finding seats for trip with id: {}", tripId);

        String sql = "SELECT * FROM seats WHERE trip_id = ?";
        List<Seat> seats = new ArrayList<>();

        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, tripId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    seats.add(buildSeatFromResultSet(result));
                }
            }

        } catch (Exception e) {
            logger.error("Error finding seats for trip {}", tripId, e);
        }

        return seats;
    }

    @Override
    public List<Seat> findSeatsForReservation(Long reservationId) {
        logger.info("Finding seats for reservation with id: {}", reservationId);

        String sql = "SELECT * FROM seats WHERE reservation_id = ?";
        List<Seat> seats = new ArrayList<>();

        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, reservationId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    seats.add(buildSeatFromResultSet(result));
                }
            }

        } catch (Exception e) {
            logger.error("Error finding seats for reservation {}", reservationId, e);
        }

        return seats;
    }

    @Override
    public int countAvailableSeatsForTrip(Long tripId) {
        logger.info("Finding available seats for trip with id: {}", tripId);
        String sql = "SELECT COUNT(*) AS available_seats FROM seats WHERE trip_id = ? AND reservation_id IS NULL";

        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, tripId);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    int availableSeats = result.getInt("available_seats");
                    logger.trace("Available seats for trip {}: {}", tripId, availableSeats);
                    return availableSeats;
                }
            }
        }catch (Exception e) {
            logger.error("Error counting available seats for trip {}", tripId, e);
        }
        return -1;
    }

    @Override
    public Long add(Seat entity) {
        logger.info("Adding seat: {}", entity);

        String sql = "INSERT INTO seats (trip_id, reservation_id, number) VALUES (?, ?, ?)";

        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, entity.getTripId());
            statement.setObject(2, entity.getReservationId(), Types.BIGINT);
            statement.setInt(3, entity.getNumber());

            int result = statement.executeUpdate();
            logger.trace("Seat inserted successfully: {}", result);

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    entity.setId(id);
                    return id;
                }
            }
            return null;
        } catch (Exception e) {
            logger.error("Error inserting seat {}", entity, e);
            return null;
        }
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting seat {}", id);

        String sql = "DELETE FROM seats WHERE id = ?";

        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            int result = statement.executeUpdate();

            logger.trace("Seat deleted successfully: {}", result);

        } catch (Exception e) {
            logger.error("Error deleting seat {}", id, e);
        }
    }

    @Override
    public void update(Seat entity) {
        logger.info("Updating seat {}", entity);

        String sql = "UPDATE seats SET trip_id=?, reservation_id=?, number=?, client_name =?  WHERE id=?";

        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, entity.getTripId());
            statement.setObject(2, entity.getReservationId(), Types.BIGINT);
            statement.setInt(3, entity.getNumber());
            statement.setObject(4, entity.getClientName(), Types.VARCHAR);
            statement.setLong(5, entity.getId());

            int result = statement.executeUpdate();
            logger.trace("Seat updated successfully: {}", result);

        } catch (Exception e) {
            logger.error("Error updating seat {}", entity, e);
        }
    }

    @Override
    public Seat findById(Long id) {
        logger.info("Finding seat {}", id);

        String sql = "SELECT * FROM seats WHERE id=?";

        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return buildSeatFromResultSet(result);
                }
            }

        } catch (Exception e) {
            logger.error("Error finding seat {}", id, e);
        }

        return null;
    }

    @Override
    public List<Seat> findAll() {
        logger.info("Finding all seats");

        String sql = "SELECT * FROM seats";
        List<Seat> seats = new ArrayList<>();

        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                seats.add(buildSeatFromResultSet(result));
            }

        } catch (Exception e) {
            logger.error("Error finding all seats", e);
        }

        return seats;
    }

    private Seat buildSeatFromResultSet(ResultSet result) throws SQLException {

        Long id = result.getLong("id");
        int number = result.getInt("number");
        Long reservationId = result.getLong("reservation_id");
        if (result.wasNull()) {
            reservationId = null;
        }
        Long tripId = result.getLong("trip_id");
        String clientName = result.getString("client_name");
        if(clientName == null)
            clientName = "-";


        return new Seat(id, number, reservationId, tripId, clientName);
    }
}

