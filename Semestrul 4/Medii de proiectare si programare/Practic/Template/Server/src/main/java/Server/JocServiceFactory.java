package Server;

import Repository.ConfiguratieHibernateRepository;
import Repository.CuvantHibernateRepository;
import Repository.JocHibernateRepository;
import Repository.JucatorHibernateRepository;
import Services.IJocServices;

public class JocServiceFactory {
    private static IJocServices instance;

    private JocServiceFactory() {}

    public static synchronized IJocServices getInstance() {
        if (instance == null) {
            JucatorHibernateRepository jucatorRepo = new JucatorHibernateRepository();
            ConfiguratieHibernateRepository configuratieRepo = new ConfiguratieHibernateRepository();
            JocHibernateRepository jocRepo = new JocHibernateRepository();
            CuvantHibernateRepository cuvantRepo = new CuvantHibernateRepository();
            instance = new JocServicesImpl(jucatorRepo, configuratieRepo, jocRepo, cuvantRepo ,3);
        }
        return instance;
    }
}
