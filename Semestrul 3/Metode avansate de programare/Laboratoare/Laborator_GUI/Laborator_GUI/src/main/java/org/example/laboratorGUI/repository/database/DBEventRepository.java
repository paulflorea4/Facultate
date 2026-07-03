package org.example.laboratorGUI.repository.database;

import org.example.laboratorGUI.domain.user.duck.SwimmingDuck;
import org.example.laboratorGUI.events.Event;
import org.example.laboratorGUI.events.RaceEvent;
import org.example.laboratorGUI.exceptions.RepositoryException;
import org.example.laboratorGUI.repository.Repository;
import org.example.laboratorGUI.utils.types.TipEvent;
import org.example.laboratorGUI.utils.types.TipRata;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBEventRepository implements Repository<Long, Event> {
    private final Connection connection;

    public DBEventRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(Event event) {
        String query = "INSERT INTO Events(name, type) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, event.getName());
            stmt.setString(2, event.getType().toString());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                long generatedID = rs.getLong(1);
                event.setId(generatedID);
            }

            if (event instanceof RaceEvent raceEvent) {
                saveRace(raceEvent);
            }
        } catch (SQLException exception){
            throw new RepositoryException("Could not save event:\n" + exception.getMessage());
        }
    }

    private void saveRace(RaceEvent event) {
        String query = "INSERT INTO RaceEvents(eventID, distances) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            Array distArray = connection.createArrayOf("BIGINT", event.getDistances().toArray());
            stmt.setLong(1, event.getEventID());
            stmt.setArray(2, distArray);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save race event:\n" + exception.getMessage());
        }
    }

    public void saveResultsOfRace(Long eventID, String results) {
        String query = "UPDATE RaceEvents SET results = ? WHERE eventID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, results);
            stmt.setLong(2, eventID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save results:\n" + exception.getMessage());
        }
    }

    public void saveDuckToRace(Long eventID, Long duckID) {
        String query = "UPDATE RaceEvents SET ducks = array_append(ducks, ?) WHERE eventID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, duckID);
            stmt.setLong(2, eventID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save duck to event:\n" + exception.getMessage());
        }
    }

    public void deleteDuckFromRace(Long eventID, Long duckID) {
        String query = "UPDATE RaceEvents SET ducks = array_remove(ducks, ?) WHERE eventID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, duckID);
            stmt.setLong(2, eventID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not delete duck from event:\n" + exception.getMessage());
        }
    }

    public void delete(Long eventID) {
        String query = "DELETE FROM Events WHERE eventID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setLong(1, eventID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not delete event:\n" + exception.getMessage());
        }
    }

    private List<SwimmingDuck> findDucksByIDs(Long[] duckIDs) {
        String query = """
        SELECT u.userID, u.username, u.email, u.password,
               d.type, d.speed, d.resistance, d.flockID
        FROM Users u
        JOIN Ducks d ON u.userID = d.userID
        WHERE u.userID = ANY (?)
        """;

        List<SwimmingDuck> ducks = new ArrayList<>();
        if (duckIDs == null || duckIDs.length == 0)
            return ducks;

        try (PreparedStatement st = connection.prepareStatement(query)) {
            Array idArray = connection.createArrayOf("BIGINT", duckIDs);
            st.setArray(1, idArray);
            ResultSet rs = st.executeQuery();
            while (rs.next())
                ducks.add(mapSwimmingDuck(rs));
            return ducks;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find ducks from their IDs:\n" + exception.getMessage());
        }
    }

    private Event mapEvent(ResultSet rs) throws SQLException {
        Long eventID = rs.getLong("eventID");
        String name = rs.getString("name");
        String type = rs.getString("type");
        String status = rs.getString("status");
        Integer subscribers = rs.getInt("subscribers");

        return new Event(eventID, name, TipEvent.valueOf(type), status, subscribers);
    }

    private SwimmingDuck mapSwimmingDuck(ResultSet rs) throws SQLException {
        Long duckID = rs.getLong("userID");
        String username = rs.getString("username");
        String email = rs.getString("email");
        String password = rs.getString("password");

        TipRata type = TipRata.valueOf(rs.getString("type").toUpperCase());
        Double speed = rs.getDouble("speed");
        Double resistance = rs.getDouble("resistance");
        Long flockID = rs.getLong("flockID");

        return new SwimmingDuck(duckID, username, email, password, type, speed, resistance, flockID);
    }

    public Event findById(Long eventID) {
        String query = """
        SELECT e.eventID, e.name, e.status, r.ducks, r.distances
        FROM Events e
        JOIN RaceEvents r ON e.eventID = r.eventID
        WHERE e.eventID = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setLong(1, eventID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                String name = rs.getString("name");
                String status = rs.getString("status");
                Array ducksSQL = rs.getArray("ducks");
                Long[] duckIDs = (ducksSQL != null)
                        ? (Long[]) ducksSQL.getArray()
                        : new Long[0];
                Long[] dist = (Long[]) rs.getArray("distances").getArray();

                List<SwimmingDuck> ducks = findDucksByIDs(duckIDs);

                return new RaceEvent(eventID, name, TipEvent.Race, status, ducks, List.of(dist));
            }
            return null;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find event:\n" + exception.getMessage());
        }
    }

    public List<Event> findAll() {
        String query = """
        SELECT e.*, COUNT(*) as subscribers
        FROM Events e
        LEFT JOIN EventSubscribers es ON e.eventID = es.eventID
        GROUP BY e.eventID
        """;
        List<Event> events = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query)){
            while (rs.next())
                events.add(mapEvent(rs));
            return events;
        }
        catch (SQLException exception) {
            throw new RepositoryException("Could not find all events:\n" + exception.getMessage());
        }
    }

    public String findResultsOfRace(Long eventID) {
        String query = "SELECT results FROM RaceEvents WHERE eventID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, eventID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return rs.getString(1);
            return null;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find race results\n" + exception.getMessage());
        }
    }

    public List<Event> findEventsByStatus(String status) {
        String query = """
        SELECT e.*, COUNT(*) as subscribers
        FROM Events e
        LEFT JOIN EventSubscribers es ON e.eventID = es.eventID
        WHERE status = ?
        GROUP BY e.eventID
        """;
        List<Event> events = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                events.add(mapEvent(rs));
            return events;
        }
        catch (SQLException exception) {
            throw new RepositoryException("Could not find events by status:\n" + exception.getMessage());
        }
    }

    public void saveSubscriberToEvent(Long eventID, Long userID) {
        String query = "INSERT INTO EventSubscribers(eventID, userID) VALUES(?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setLong(1, eventID);
            stmt.setLong(2, userID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save subscriber to event:\n" + exception.getMessage());
        }
    }

    public void deleteSubscriberFromEvent(Long eventID, Long userID) {
        String query = "DELETE FROM EventSubscribers WHERE eventID = ? AND userID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setLong(1, eventID);
            stmt.setLong(2, userID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not delete subscriber from event:\n" + exception.getMessage());
        }
    }

    public List<Long> findSubscriberIDsForAnEvent(Long eventID) {
        String query = "SELECT userID FROM EventSubscribers WHERE eventID = ?";
        List<Long> subscriberIDs = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setLong(1, eventID);
            var rs = stmt.executeQuery();
            while(rs.next()){
                Long subscriberID = rs.getLong(1);
                subscriberIDs.add(subscriberID);
            }
            return subscriberIDs;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find subscriber IDs for the event:\n" + exception.getMessage());
        }
    }

    public boolean findIfUserIsSubscribed(Long eventID, Long userID) {
        String query = "SELECT * FROM EventSubscribers WHERE eventID = ? AND userID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, eventID);
            stmt.setLong(2, userID);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find if user is subscribed:\n" + exception.getMessage());
        }
    }

    public void updateRaceAsFinished(Long eventID) {
        String query = "UPDATE Events SET status = 'FINISHED' WHERE eventID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, eventID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not set race as finished:\n " + exception.getMessage());
        }
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
