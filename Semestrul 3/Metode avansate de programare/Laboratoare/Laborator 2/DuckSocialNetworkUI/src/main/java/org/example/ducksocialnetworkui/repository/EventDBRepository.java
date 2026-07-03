package org.example.ducksocialnetworkui.repository;

import org.example.ducksocialnetworkui.domain.SwimmingDuck;
import org.example.ducksocialnetworkui.dto.Dto;
import org.example.ducksocialnetworkui.event.Event;
import org.example.ducksocialnetworkui.event.TipEvent;
import org.example.ducksocialnetworkui.exception.RepositoryException;
import org.example.ducksocialnetworkui.event.RaceEvent;
import org.example.ducksocialnetworkui.utils.paging.Pageable;
import org.example.ducksocialnetworkui.utils.paging.Page;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventDBRepository implements EventRepository {

    private final String url;
    private final String username;
    private final String password;

    public EventDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Event> save(Event event) {
        String query = "INSERT INTO events(name, type,status) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, event.getName());
                stmt.setString(2, event.getType().toString());
                stmt.setString(3, event.getStatus());
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    long generatedID = rs.getLong(1);
                    event.setId(generatedID);
                }

                if (event instanceof RaceEvent raceEvent) {
                    return saveRace(connection, raceEvent);
                }
                return Optional.empty();
        } catch (SQLException exception){
            throw new RepositoryException("Could not save event:\n" + exception.getMessage());
        }
    }

    private Optional<Event> saveRace(Connection connection, RaceEvent event) {
        String query = "INSERT INTO race_events(event_id, distances) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            Array distArray = connection.createArrayOf("BIGINT", event.getDistances().toArray());
            stmt.setLong(1, event.getEventID());
            stmt.setArray(2, distArray);
            stmt.executeUpdate();

            return  Optional.of(event);
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save race event:\n" + exception.getMessage());
        }
    }

    @Override
    public Optional<Event> findOne(Long id) {
        String query = """
        SELECT e.id, e.name, e.status, r.distances
        FROM events e
        JOIN race_events r ON e.id = r.event_id
        WHERE e.id = ?
        """;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                String name = rs.getString("name");
                String status = rs.getString("status");
                Long[] dist = (Long[]) rs.getArray("distances").getArray();

                List<SwimmingDuck> ducks = loadDucksForEvent(connection,id);

                return Optional.of(new RaceEvent(id, name, status, List.of(dist), ducks));
            }
            return Optional.empty();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find event:\n" + exception.getMessage());
        }
    }

    @Override
    public Optional<Event> delete(Long id) {
        Optional<Event> existing = findOne(id);
        if (existing.isEmpty())
            return Optional.empty();

        String sql = "DELETE FROM events WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
            return existing;

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    private List<SwimmingDuck> loadDucksForEvent(Connection connection, long eventId) throws SQLException {
        String sql = """
            SELECT u.id, u.username, u.email, u.password,
                   d.viteza, d.rezistenta, d.card_id
            FROM race_event_ducks red
            JOIN ducks d ON red.duck_id = d.user_id
            JOIN users u ON u.id = d.user_id
            WHERE red.race_event_id = ?
        """;

        List<SwimmingDuck> ducks = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, eventId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                long uid = rs.getLong("id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                double viteza = rs.getDouble("viteza");
                double rezistenta = rs.getDouble("rezistenta");
                long cardId = rs.getLong("card_id");

                ducks.add(new SwimmingDuck(uid, username, email, password, viteza, rezistenta, cardId));
            }
        }

        return ducks;
    }

    @Override
    public Iterable<Event> findAll() {
        String sql = """
            SELECT e.*,COUNT(*) as subscribers
            FROM events e
            LEFT JOIN event_subscribers es ON e.id = es.event_id
            GROUP BY e.id
        """;

        List<Event> events = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                events.add(buildEvent(rs, connection));
            }

        } catch (SQLException e) {
            throw new RepositoryException("Could not find all events:\n" + e.getMessage());
        }

        return events;
    }

    private Event buildEvent(ResultSet rs, Connection connection) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String type = rs.getString("type");
        String status = rs.getString("status");
        Integer subscribers=rs.getInt("subscribers");

        return new Event(id, name, TipEvent.valueOf(type), status, subscribers);
    }

    @Override
    public void addSubscriber(long eventId, long userId) {
        String sql = "INSERT INTO event_subscribers(event_id, user_id) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, eventId);
            ps.setLong(2, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException("Could not save subscriber to event:\n" + e.getMessage());
        }
    }

    @Override
    public void removeSubscriber(long eventId, long userId) {
        String sql = "DELETE FROM event_subscribers WHERE event_id = ? AND user_id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, eventId);
            ps.setLong(2, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException("Could not delete subscriber from event:\n" + e.getMessage());
        }
    }

    @Override
    public List<Long> getSubscribers(long eventId) {
        String sql = "SELECT user_id FROM event_subscribers WHERE event_id = ?";
        List<Long> list = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, eventId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getLong("user_id"));
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }

        return list;
    }

    @Override
    public void saveResults(Long eventId, String results) {
        String query = "UPDATE race_events SET results = ? WHERE event_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, results);
                stmt.setLong(2, eventId);
                stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save results:\n" + exception.getMessage());
        }
    }

    @Override
    public void saveDuckToRace(Long eventId, Long duckId) {
        String query = "INSERT INTO race_event_ducks(race_event_id, duck_id) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, eventId);
                stmt.setLong(2, duckId);
                stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save duck to event:\n" + exception.getMessage());
        }
    }

    @Override
    public void deleteDuckFromRace(Long eventId, Long duckId) {
        String query = "DELETE FROM race_event_ducks WHERE duck_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, duckId);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not delete duck from event:\n" + exception.getMessage());
        }
    }

    @Override
    public String findResultsOfRace(Long eventID) {
        String query = "SELECT results FROM race_events WHERE event_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, eventID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next())
                    return rs.getString(1);
                return null;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find race results\n" + exception.getMessage());
        }
    }

    @Override
    public List<Event> findEventsByStatus(String status) {
        String query = """
        SELECT e.*, COUNT(es.user_id) AS subscribers
        FROM events e
        LEFT JOIN event_subscribers es ON e.id = es.event_id
        WHERE e.status = ?
        GROUP BY e.id
        """;
        List<Event> events = new ArrayList<>();
        try (Connection connection=DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, status);
                ResultSet rs = stmt.executeQuery();
                while (rs.next())
                    events.add(buildEvent(rs,connection));
                return events;
        }
        catch (SQLException exception) {
            throw new RepositoryException("Could not find events by status:\n" + exception.getMessage());
        }
    }

    @Override
    public void updateStatus(Long eventId, String status) {
        String query = "UPDATE events SET status = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, status);
                stmt.setLong(2, eventId);
                stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not update the status:\n " + exception.getMessage());
        }
    }

    public boolean findIfUserIsSubscribed(Long eventID, Long userID) {
        String query = "SELECT * FROM event_subscribers WHERE event_id = ? AND user_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, eventID);
            stmt.setLong(2, userID);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find if user is subscribed:\n" + exception.getMessage());
        }
    }

    @Override
    public Page<Event> getAllPage(Pageable page) {
        return null;
    }

    @Override
    public Page<Event> getAllPage(Pageable page, Dto filter) {
        return null;
    }
}
