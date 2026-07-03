package Repository;

import Domain.Joc;

import java.util.List;

public class JocHibernateRepository
        extends AbstractHibernateRepository<Long, Joc>
        implements JocRepository {

    public JocHibernateRepository() {
        super(HibernateUtils.getSessionFactory(), Joc.class);
    }

    @Override
    public Iterable<Joc> findAllGamesWithDetails() {
        logger.traceEntry("findAllGamesWithDetails");
        List<Joc> result = sessionFactory.fromTransaction(session ->
                session.createQuery(
                        "FROM Joc j JOIN FETCH j.jucator JOIN FETCH j.configuratie",
                        Joc.class
                ).list());
        return logger.traceExit(result);
    }
}
