package Repository;

import Domain.Configuratie;

import java.util.List;
import java.util.Random;

public class ConfiguratieHibernateRepository
        extends AbstractHibernateRepository<Long, Configuratie>
        implements ConfiguratieRepository {

    private static final Random random = new Random();

    public ConfiguratieHibernateRepository() {
        super(HibernateUtils.getSessionFactory(), Configuratie.class);
    }

    @Override
    public Configuratie getRandomConfiguratie(int n) {
        logger.traceEntry("getRandomConfiguratie");
        List<Configuratie> all = sessionFactory.fromTransaction(session ->
                session.createQuery("FROM Configuratie WHERE n = :n", Configuratie.class)
                        .setParameter("n", n)
                        .list());
        if (all == null || all.isEmpty()) {
            logger.warn("Nu există configurații pentru n = {} în baza de date.", n);
            return null;
        }
        Configuratie result = all.get(random.nextInt(all.size()));
        return logger.traceExit(result);
    }
}
