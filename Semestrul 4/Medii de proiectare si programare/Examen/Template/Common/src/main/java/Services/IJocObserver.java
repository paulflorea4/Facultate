package Services;

import Domain.Configuratie;

import java.util.List;

public interface IJocObserver {
    void clasamentActualizat() throws Exception;
    void jocPornit(List<Configuratie> configuratii, String mesaj) throws Exception;
    void incepeRunda(String jucatorActiv) throws Exception;
    void variantaAleasa(Configuratie configuratie) throws Exception;
}
