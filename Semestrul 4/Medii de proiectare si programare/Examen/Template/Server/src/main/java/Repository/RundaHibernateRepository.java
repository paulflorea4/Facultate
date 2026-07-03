package Repository;

import Domain.Jucator;
import Domain.Runda;

import java.util.List;

public class RundaHibernateRepository
        extends AbstractHibernateRepository<Long, Runda>
        implements RundaRepository{

    public RundaHibernateRepository() {
        super(HibernateUtils.getSessionFactory(), Runda.class);
    }


    @Override
    public List<Runda> findRundeByJocId(Long jocId) {
        logger.traceEntry("findRundeByJocId jocId={}", jocId);
        List<Runda> result = sessionFactory.fromTransaction(session ->
                session.createQuery("FROM Runda WHERE joc.id = :jocId", Runda.class)
                        .setParameter("jocId", jocId)
                        .list());
        return logger.traceExit(result);
    }
}
