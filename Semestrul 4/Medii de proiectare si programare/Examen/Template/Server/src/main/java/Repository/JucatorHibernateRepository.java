package Repository;

import Domain.Jucator;

public class JucatorHibernateRepository
        extends AbstractHibernateRepository<Long, Jucator>
        implements JucatorRepository {

    public JucatorHibernateRepository() {
        super(HibernateUtils.getSessionFactory(), Jucator.class);
    }

    @Override
    public Jucator findByAlias(String alias) {
        logger.traceEntry("findByAlias alias={}", alias);
        Jucator result = sessionFactory.fromTransaction(session ->
                session.createQuery("FROM Jucator WHERE alias = :alias", Jucator.class)
                        .setParameter("alias", alias)
                        .uniqueResult());
        return logger.traceExit(result);
    }
}
