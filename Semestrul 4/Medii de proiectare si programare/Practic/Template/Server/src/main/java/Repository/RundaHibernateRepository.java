package Repository;

import Domain.Runda;

public class RundaHibernateRepository
        extends AbstractHibernateRepository<Long, Runda>
        implements RundaRepository{

    public RundaHibernateRepository() {
        super(HibernateUtils.getSessionFactory(), Runda.class);
    }
}
