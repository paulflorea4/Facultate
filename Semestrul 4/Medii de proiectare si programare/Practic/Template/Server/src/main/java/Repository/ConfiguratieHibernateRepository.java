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
    public Configuratie getRandomConfiguratie() {
        logger.traceEntry("getRandomConfiguratie");
        List<Configuratie> all = sessionFactory.fromTransaction(session ->
                session.createQuery("FROM Configuratie", Configuratie.class).list());
        if (all == null || all.isEmpty()) {
            logger.warn("Nu există configurații în baza de date.");
            return null;
        }
        Configuratie result = all.get(random.nextInt(all.size()));
        return logger.traceExit(result);
    }
}
