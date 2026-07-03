package Repository;

import Domain.Configuratie;
import Domain.Cuvant;

import java.util.List;
import java.util.Random;

public class CuvantHibernateRepository extends AbstractHibernateRepository<Long, Cuvant>
        implements CuvantRepository{

    private static final Random random = new Random();

    public CuvantHibernateRepository() {
        super(HibernateUtils.getSessionFactory(), Cuvant.class);
    }

    @Override
    public Cuvant getRandomCuvant(String complexitate) {
        logger.traceEntry("getRandomConfiguratie");
        List<Cuvant> all = sessionFactory.fromTransaction(session ->
                session.createQuery("FROM Cuvant WHERE complexitate = :complexitate", Cuvant.class)
                        .setParameter("complexitate", complexitate).list());
        if (all == null || all.isEmpty()) {
            logger.warn("Nu există configurații în baza de date.");
            return null;
        }
        Cuvant result = all.get(random.nextInt(all.size()));
        return logger.traceExit(result);
    }
}
