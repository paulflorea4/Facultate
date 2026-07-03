package transport.persistence.repository.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import transport.persistence.repository.ReservationRepository;
import transport.persistence.repository.SeatRepository;
import transport.persistence.repository.TripRepository;
import transport.persistence.repository.utils.JdbcUtils;
import transport.model.Reservation;
import transport.model.Seat;
import transport.model.Trip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReservationDbRepository implements ReservationRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public ReservationDbRepository(Properties properties) {
        logger.info("Initializind ReservationDbRepository with properties: {} ", properties);
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public Iterable<Reservation> findReservationsByClientName(String clientName) {
        logger.info("Finding reservations for client: {}", clientName);
        String sql = "SELECT * FROM reservations WHERE client_name = ?";
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, clientName);
            try (ResultSet result = statement.executeQuery()) {
                List<Reservation> reservations = new ArrayList<>();
                while (result.next()) {
                    Reservation reservation = buildReservationFromResultSet(result);
                    reservations.add(reservation);
                    logger.trace("Found reservation: {}", reservation);
                }
                return reservations;
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error processing reservations result: " + e);
        }
        return new ArrayList<>();
    }

    @Override
    public Long add(Reservation entity) {
        logger.info("Adding new reservation: {}", entity);
        String sql = "INSERT INTO reservations (client_name, trip_id, number_of_seats) VALUES (?, ?, ?)";
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getClientName());
            statement.setLong(2, entity.getTripId());
            statement.setInt(3, entity.getNumberOfSeats());
            int result = statement.executeUpdate();
            logger.trace("Reservation added successfully, result: {}", result);

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
            System.out.println("Error adding reservation: " + e);
            return null;
        } finally {
            logger.traceExit();
        }
    }

    @Override
    public void delete(Long aLong) {
        logger.info("Deleting reservation: {}", aLong);
        String sql = "DELETE FROM reservations WHERE id = ?";
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, aLong);
            int result = statement.executeUpdate();
            logger.trace("Reservation deleted successfully, result: {}", result);
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error deleting reservation: " + e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Reservation entity) {
        logger.info("Updating reservation: {}", entity);
        String sql = "UPDATE reservations SET client_name = ?, trip_id = ?, number_of_seats = ? WHERE id = ?";
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getClientName());
            statement.setLong(2, entity.getTripId());
            statement.setInt(3, entity.getNumberOfSeats());
            statement.setLong(4, entity.getId());
            int result = statement.executeUpdate();
            logger.trace("Reservation updated successfully, result: {}", result);
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error updating reservation: " + e);
        }
        logger.traceExit();
    }

    private Reservation buildReservationFromResultSet(ResultSet result) throws SQLException {
        Long id = result.getLong("id");
        String clientName = result.getString("client_name");
        Long tripId = result.getLong("trip_id");
        int numberOfSeats = result.getInt("number_of_seats");
        Reservation reservation = new Reservation(id, clientName, tripId, numberOfSeats);
        reservation.setId(id);
        return reservation;
    }

    @Override
    public Reservation findById(Long aLong) {
        logger.info("Finding reservation: {}", aLong);
        String sql = "SELECT * FROM reservations WHERE id = ?";
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, aLong);
            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    Reservation reservation = buildReservationFromResultSet(result);
                    logger.trace("Reservation found: {}", reservation);
                    return reservation;
                }else{
                    logger.trace("No reservation found with id: {}", aLong);
                    return null;
                }
            }
        }catch (SQLException e) {
            logger.error(e);
            System.out.println("Error processing reservation result: " + e);
            return  null;
        }
    }

    @Override
    public List<Reservation> findAll() {
        logger.info("Finding all reservations");
        String sql = "SELECT * FROM reservations";
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            try (var result = statement.executeQuery()) {
                List<Reservation> reservations = new ArrayList<>();
                while (result.next()) {
                    Reservation reservation = buildReservationFromResultSet(result);
                    reservations.add(reservation);
                    logger.trace("Found reservation: {}", reservation);
                }
                return reservations;
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error processing reservations result: " + e);
        }
        return new ArrayList<>();
    }

    public Long makeReservation(Reservation reservation, Trip trip, SeatRepository seatRepository, TripRepository tripRepository) {
        logger.info("Starting make reservation transaction for trip: {}", trip.getId());
        Connection connection = dbUtils.getConnection();
        try {
            connection.setAutoCommit(false);

            Long reservationId;
            String insertSql = "INSERT INTO reservations (client_name, trip_id, number_of_seats) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, reservation.getClientName());
                statement.setLong(2, reservation.getTripId());
                statement.setInt(3, reservation.getNumberOfSeats());
                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (!generatedKeys.next()) {
                        throw new SQLException("Failed to retrieve reservation ID");
                    }
                    reservationId = generatedKeys.getLong(1);
                }
            }

            List<Long> availableSeatIds = getAvailableSeatIds(connection, trip.getId(), reservation.getNumberOfSeats());
            if (availableSeatIds.size() < reservation.getNumberOfSeats()) {
                throw new SQLException("Not enough available seats");
            }

            String seatUpdateSql = "UPDATE seats SET reservation_id = ?, client_name = ? WHERE id = ? AND reservation_id IS NULL";
            try (PreparedStatement seatStatement = connection.prepareStatement(seatUpdateSql)) {
                for (Long seatId : availableSeatIds) {
                    seatStatement.setLong(1, reservationId);
                    seatStatement.setString(2, reservation.getClientName());
                    seatStatement.setLong(3, seatId);
                    int updated = seatStatement.executeUpdate();
                    if (updated != 1) {
                        throw new SQLException("Seat " + seatId + " is no longer available");
                    }
                }
            }

            String tripUpdateSql = "UPDATE trips SET available_seats = available_seats - ? WHERE id = ? AND available_seats >= ?";
            try (PreparedStatement tripStatement = connection.prepareStatement(tripUpdateSql)) {
                tripStatement.setInt(1, reservation.getNumberOfSeats());
                tripStatement.setLong(2, trip.getId());
                tripStatement.setInt(3, reservation.getNumberOfSeats());
                int updated = tripStatement.executeUpdate();
                if (updated != 1) {
                    throw new SQLException("Trip seats could not be updated");
                }
            }

            reservation.setId(reservationId);
            connection.commit();
            logger.info("Reservation transaction committed successfully with ID: {}", reservationId);
            return reservationId;
        } catch (SQLException e) {
            logger.error("Error in make reservation transaction, rolling back", e);
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Error rolling back transaction", rollbackEx);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Error resetting autocommit", e);
            }
            logger.traceExit();
        }
        return null;
    }

    public void cancelReservationTransaction(Reservation reservation, SeatRepository seatRepository, TripRepository tripRepository) {
        logger.info("Starting cancel reservation transaction for reservation: {}", reservation.getId());
        Connection connection = dbUtils.getConnection();
        try {
            connection.setAutoCommit(false);

            Long tripId;
            int reservedSeats;
            String reservationSql = "SELECT trip_id, number_of_seats FROM reservations WHERE id = ?";
            try (PreparedStatement reservationStatement = connection.prepareStatement(reservationSql)) {
                reservationStatement.setLong(1, reservation.getId());
                try (ResultSet rs = reservationStatement.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Reservation not found");
                    }
                    tripId = rs.getLong("trip_id");
                    reservedSeats = rs.getInt("number_of_seats");
                }
            }

            String seatResetSql = "UPDATE seats SET reservation_id = NULL, client_name = NULL WHERE reservation_id = ?";
            try (PreparedStatement seatStatement = connection.prepareStatement(seatResetSql)) {
                seatStatement.setLong(1, reservation.getId());
                seatStatement.executeUpdate();
            }

            String deleteSql = "DELETE FROM reservations WHERE id = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
                deleteStatement.setLong(1, reservation.getId());
                int deleted = deleteStatement.executeUpdate();
                if (deleted != 1) {
                    throw new SQLException("Reservation not found for delete");
                }
            }

            String tripUpdateSql = "UPDATE trips SET available_seats = available_seats + ? WHERE id = ?";
            try (PreparedStatement tripStatement = connection.prepareStatement(tripUpdateSql)) {
                tripStatement.setInt(1, reservedSeats);
                tripStatement.setLong(2, tripId);
                int updated = tripStatement.executeUpdate();
                if (updated != 1) {
                    throw new SQLException("Trip seats could not be restored");
                }
            }

            connection.commit();
            logger.info("Cancel reservation transaction committed successfully");
        } catch (SQLException e) {
            logger.error("Error in cancel reservation transaction, rolling back", e);
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Error rolling back transaction", rollbackEx);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Error resetting autocommit", e);
            }
            logger.traceExit();
        }
    }

    private List<Long> getAvailableSeatIds(Connection connection, Long tripId, int seatsNeeded) throws SQLException {
        List<Long> seatIds = new ArrayList<>();
        String sql = "SELECT id FROM seats WHERE trip_id = ? AND reservation_id IS NULL ORDER BY number LIMIT ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, tripId);
            statement.setInt(2, seatsNeeded);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    seatIds.add(result.getLong("id"));
                }
            }
        }

        return seatIds;
    }
}
