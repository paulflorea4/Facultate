package transport.persistence.repository.hibernate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import transport.persistence.repository.TripRepository;
import transport.model.Trip;
import transport.persistence.repository.utils.HibernateUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TripHibernateRepository implements TripRepository {

    private static final Logger logger = LogManager.getLogger(TripHibernateRepository.class);

    @Override
    public Long add(Trip entity) {
        logger.info("Adding trip: {}", entity);

        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(entity);

            transaction.commit();
            return entity.getId();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error adding trip", e);
            System.out.println("Error adding trip: " + e);
            return null;
        }
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting trip with id: {}", id);

        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Trip trip = session.find(Trip.class, id);
            if (trip != null) {
                session.remove(trip);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting trip", e);
            System.out.println("Error deleting trip: " + e);
        }
    }

    @Override
    public void update(Trip entity) {
        logger.info("Updating trip: {}", entity);

        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(entity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating trip", e);
            System.out.println("Error updating trip: " + e);
        }
    }

    @Override
    public Trip findById(Long id) {
        logger.info("Finding trip by id: {}", id);

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.find(Trip.class, id);
        } catch (Exception e) {
            logger.error("Error finding trip by id", e);
            System.out.println("Error finding trip: " + e);
            return null;
        }
    }

    @Override
    public List<Trip> findAll() {
        logger.info("Finding all trips");

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Trip", Trip.class).list();
        } catch (Exception e) {
            logger.error("Error finding all trips", e);
            System.out.println("Error finding trips: " + e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Trip> findTripsByDestinationAndDepartureDate(String destination, String date, String hour) {
        logger.info("Finding trips by destination: {}, date: {}, hour: {}", destination, date, hour);

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from Trip where destination = :destination and date = :date and hour = :hour",
                    Trip.class)
                    .setParameter("destination", destination)
                    .setParameter("date", date)
                    .setParameter("hour", hour)
                    .list();
        } catch (Exception e) {
            logger.error("Error finding filtered trips", e);
            System.out.println("Error finding trips: " + e);
            return new ArrayList<>();
        }
    }
    
}
