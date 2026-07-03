package Server;

import Domain.Configuratie;
import Domain.Cuvant;
import Domain.Joc;
import Domain.Jucator;
import Repository.ConfiguratieRepository;
import Repository.CuvantRepository;
import Repository.JocRepository;
import Repository.JucatorRepository;
import Services.IJocObserver;
import Services.IJocServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JocServicesImpl implements IJocServices {
    private final JucatorRepository jucatorRepo;
    private final ConfiguratieRepository configuratieRepo;
    private final JocRepository jocRepo;
    private final CuvantRepository cuvantRepo;

    private final Map<Long, IJocObserver> loggedClients = new ConcurrentHashMap<>();
    private final Map<Long, Jucator> loggedJucatori = new ConcurrentHashMap<>();
    private final Map<Long, IJocObserver> waitingClients = new ConcurrentHashMap<>();

    private final Map<Long, IJocObserver> observatoriJocActiv = new ConcurrentHashMap<>();
    private final List<Jucator> jucatoriActiviInJoc = new ArrayList<>();
    private int rundaCurenta = 0;

    private static final Logger logger = LogManager.getLogger();
    private final Random random = new Random();
    private final int M;

    public JocServicesImpl(JucatorRepository jucatorRepo,
                           ConfiguratieRepository configuratieRepo,
                           JocRepository jocRepo,
                           CuvantRepository cuvantRepo,
                           int M) {
        this.jucatorRepo = jucatorRepo;
        this.configuratieRepo = configuratieRepo;
        this.jocRepo = jocRepo;
        this.cuvantRepo = cuvantRepo;
        this.M = M;
    }

    @Override
    public Jucator login(String alias, IJocObserver client) throws Exception {
        logger.traceEntry("login alias={}", alias);
        Jucator jucator = jucatorRepo.findByAlias(alias);
        if (jucator == null) {
            throw new Exception("Jucătorul cu aliasul '" + alias + "' nu există.");
        }
        if (loggedClients.containsKey(jucator.getId())) {
            throw new Exception("Jucătorul este deja logat.");
        }
        loggedClients.put(jucator.getId(), client);
        loggedJucatori.put(jucator.getId(), jucator);
        waitingClients.put(jucator.getId(), client);
        logger.info("Jucător intrat în lobby: {}", alias);

        if (waitingClients.size() == M) {
            logger.info("S-au adunat {} jucători. Jocul pornește!", M);

            jucatoriActiviInJoc.clear();
            observatoriJocActiv.clear();
            rundaCurenta = 0;

            for (Long id : waitingClients.keySet()) {
                jucatoriActiviInJoc.add(loggedJucatori.get(id));
                observatoriJocActiv.put(id, waitingClients.get(id));
            }

            String aliasUrmator = "";
            int max=-1;
            for(Jucator j : jucatoriActiviInJoc){
                if(j.getAlias().length()>=max){
                    max=j.getAlias().length();
                    aliasUrmator=j.getAlias();
                }
            }

            for (IJocObserver obs : observatoriJocActiv.values()) {
                try {
                    obs.jocPornit(null, "Jocul a început");
                    obs.incepeRunda(aliasUrmator);
                } catch (Exception e) {
                    logger.error("Eroare notificare pornire joc/rundă: {}", e.getMessage());
                }
            }
            waitingClients.clear();
        }

        return logger.traceExit(jucator);
    }

    @Override
    public void logout(Jucator jucator, IJocObserver client) throws Exception {
        logger.traceEntry("logout jucator={}", jucator);
        loggedClients.remove(jucator.getId());
        logger.info("Jucător delogat: {}", jucator.getAlias());
        logger.traceExit();
    }

    @Override
    public Configuratie incepeJoc() throws Exception {
        logger.traceEntry("incepeJoc");
        return logger.traceExit(configuratieRepo.getRandomConfiguratie());
    }

    @Override
    public void salveazaJoc(Jucator jucator, Configuratie configuratie, int punctaj, int durataSauIncercari) throws Exception {
        logger.traceEntry("salveazaJoc jucator={}, punctaj={}, durataSauIncercari={}", jucator, punctaj, durataSauIncercari);
        Joc joc = new Joc(jucator, configuratie, LocalDateTime.now(), punctaj, durataSauIncercari);
        jocRepo.save(joc);
        notifyAllClients();
        logger.traceExit();
    }

    @Override
    public Iterable<Joc> getClasament() throws Exception {
        logger.traceEntry("getClasament");
        return logger.traceExit(jocRepo.findAllGamesWithDetails());
    }

    @Override
    public void alegeVarianta(Jucator jucator) throws Exception {
        logger.traceEntry("alegeVarianta jucator={}", jucator);

        Cuvant cuvantGenerat = cuvantRepo.getRandomCuvant("usor");

        for (IJocObserver obs : observatoriJocActiv.values()) {
            try {
                obs.variantaAleasa(cuvantGenerat);
            } catch (Exception e) {
                logger.error("Eroare la notificarea cuvantului selectat: {}", e.getMessage());
            }
        }

        logger.traceExit("Varianta generata si trimisa cu succes: {}", cuvantGenerat);
    }

    @Override
    public void close() {
        logger.info("Închidere servicii...");
        jucatorRepo.close();
        configuratieRepo.close();
        jocRepo.close();
    }

    private void notifyAllClients() {
        logger.traceEntry("notifyAllClients");
        for (Map.Entry<Long, IJocObserver> entry : loggedClients.entrySet()) {
            try {
                entry.getValue().clasamentActualizat();
            } catch (Exception e) {
                logger.error("Eroare la notificarea clientului id={}: {}", entry.getKey(), e.getMessage());
            }
        }
        logger.traceExit();
    }
}
