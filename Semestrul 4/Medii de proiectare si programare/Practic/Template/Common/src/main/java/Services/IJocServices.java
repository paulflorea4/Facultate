package Services;

import Domain.Configuratie;
import Domain.Joc;
import Domain.Jucator;

public interface IJocServices {
    Jucator login(String alias, IJocObserver client) throws Exception;
    void logout(Jucator jucator, IJocObserver client) throws Exception;
    Configuratie incepeJoc() throws Exception;
    void salveazaJoc(Jucator jucator, Configuratie configuratie, int punctaj, int durataSauIncercari) throws Exception;
    Iterable<Joc> getClasament() throws Exception;
    void alegeVarianta(Jucator jucator) throws Exception;
    void close();
}
