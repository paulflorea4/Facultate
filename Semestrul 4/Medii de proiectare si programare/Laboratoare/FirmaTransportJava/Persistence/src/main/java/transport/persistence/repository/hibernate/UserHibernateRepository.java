package transport.persistence.repository.hibernate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import transport.model.User;
import transport.persistence.repository.UserRepository;
import transport.persistence.repository.utils.HibernateUtils;
import transport.persistence.repository.utils.PasswordUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserHibernateRepository implements UserRepository {

	private static final Logger logger = LogManager.getLogger(UserHibernateRepository.class);

	@Override
	public Long add(User entity) {
		logger.info("Adding user: {}", entity);

		Transaction transaction = null;
		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();

			entity.setPassword(PasswordUtils.hash(entity.getPassword()));
			session.persist(entity);

			transaction.commit();
			return entity.getId();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error("Error adding user", e);
			System.out.println("Error adding user: " + e);
			return null;
		}
	}

	@Override
	public void delete(Long id) {
		logger.info("Deleting user with id: {}", id);

		Transaction transaction = null;
		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();

			User user = session.find(User.class, id);
			if (user != null) {
				session.remove(user);
			}

			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error("Error deleting user", e);
			System.out.println("Error deleting user: " + e);
		}
	}

	@Override
	public void update(User entity) {
		logger.info("Updating user: {}", entity);

		Transaction transaction = null;
		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();

			entity.setPassword(PasswordUtils.hash(entity.getPassword()));
			session.merge(entity);

			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error("Error updating user", e);
			System.out.println("Error updating user: " + e);
		}
	}

	@Override
	public User findById(Long id) {
		logger.info("Finding user by id: {}", id);

		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			return session.find(User.class, id);
		} catch (Exception e) {
			logger.error("Error finding user by id", e);
			System.out.println("Error finding user: " + e);
			return null;
		}
	}

	@Override
	public List<User> findAll() {
		logger.info("Finding all users");

		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			return session.createQuery("from User", User.class).list();
		} catch (Exception e) {
			logger.error("Error finding all users", e);
			System.out.println("Error finding users: " + e);
			return new ArrayList<>();
		}
	}

	@Override
	public User findByUsername(String username) {
		logger.info("Finding user by username: {}", username);

		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			return session.createQuery("from User where username = :username", User.class)
					.setParameter("username", username)
					.uniqueResult();
		} catch (Exception e) {
			logger.error("Error finding user by username", e);
			System.out.println("Error during finding by username: " + e);
			return null;
		}
	}

}
