package Repository;

import Domain.Configuratie;

public interface ConfiguratieRepository extends Repository<Long, Configuratie> {
    Configuratie getRandomConfiguratie(int n);
}
