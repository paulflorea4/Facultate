package Repository;

import Domain.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractHibernateRepository<ID extends Serializable, E extends Entity<ID>>
        implements Repository<ID, E> {

    protected final SessionFactory sessionFactory;
    protected final Class<E> entityClass;
    protected final Logger logger = LogManager.getLogger(getClass());

    protected AbstractHibernateRepository(SessionFactory sessionFactory, Class<E> entityClass) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
    }

    @Override
    public E findOne(ID id) {
        logger.traceEntry("findOne id={}", id);
        E result = sessionFactory.fromTransaction(session ->
                session.get(entityClass, id));
        return logger.traceExit(result);
    }

    @Override
    public Iterable<E> findAll() {
        logger.traceEntry("findAll [{}]", entityClass.getSimpleName());
        List<E> result = sessionFactory.fromTransaction(session ->
                session.createQuery("FROM " + entityClass.getSimpleName(), entityClass).list());
        return logger.traceExit(result);
    }

    @Override
    public E save(E entity) {
        logger.traceEntry("save {}", entity);
        sessionFactory.inTransaction(session -> session.persist(entity));
        return logger.traceExit(entity);
    }

    @Override
    public E update(E entity) {
        logger.traceEntry("update {}", entity);
        E result = sessionFactory.fromTransaction(session ->
                session.merge(entity));
        return logger.traceExit(result);
    }

    @Override
    public E delete(ID id) {
        logger.traceEntry("delete id={}", id);
        return sessionFactory.fromTransaction(session -> {
            E entity = session.get(entityClass, id);
            if (entity != null) session.remove(entity);
            return entity;
        });
    }

    @Override
    public void close() {
        HibernateUtils.closeSessionFactory();
    }
}
