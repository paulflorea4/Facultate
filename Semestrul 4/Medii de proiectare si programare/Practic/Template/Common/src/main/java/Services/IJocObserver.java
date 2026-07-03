package Services;

import Domain.Configuratie;
import Domain.Cuvant;

import java.util.List;

public interface IJocObserver {
    void clasamentActualizat() throws Exception;
    void jocPornit(Cuvant cuvant, String mesaj) throws Exception;
    void incepeRunda(String jucatorActiv) throws Exception;
    void variantaAleasa(Cuvant cuvant) throws Exception;
}
