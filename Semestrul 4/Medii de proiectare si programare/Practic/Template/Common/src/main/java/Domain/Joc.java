package Domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@jakarta.persistence.Entity
@Table(name = "Jocuri")
public class Joc extends Entity<Long> implements Serializable {
    private static final long serialVersionUID = 4L;

    @ManyToOne
    @JoinColumn(name = "jucator_id")
    private Jucator jucator;

    @ManyToOne
    @JoinColumn(name = "configuratie_id")
    private Configuratie configuratie;

    @Column(name = "data_joc")
    private LocalDateTime dataJoc;

    @Column(name = "punctaj")
    private int punctaj;

    @Column(name = "durata_incercari")
    private int durataSauIncercari;

    public Joc() {}

    public Joc(Jucator jucator, Configuratie configuratie, LocalDateTime dataJoc,
               int punctaj, int durataSauIncercari) {
        this.jucator = jucator;
        this.configuratie = configuratie;
        this.dataJoc = dataJoc;
        this.punctaj = punctaj;
        this.durataSauIncercari = durataSauIncercari;
    }

    public Jucator getJucator() { return jucator; }
    public void setJucator(Jucator jucator) { this.jucator = jucator; }

    public Configuratie getConfiguratie() { return configuratie; }
    public void setConfiguratie(Configuratie configuratie) { this.configuratie = configuratie; }

    public LocalDateTime getDataJoc() { return dataJoc; }
    public void setDataJoc(LocalDateTime dataJoc) { this.dataJoc = dataJoc; }

    public int getPunctaj() { return punctaj; }
    public void setPunctaj(int punctaj) { this.punctaj = punctaj; }

    public int getDurataSauIncercari() { return durataSauIncercari; }
    public void setDurataSauIncercari(int durataSauIncercari) { this.durataSauIncercari = durataSauIncercari; }

    @Override
    public String toString() {
        return "Joc{id=" + getId() +
                ", jucator=" + jucator +
                ", configuratie=" + configuratie +
                ", dataJoc=" + dataJoc +
                ", punctaj=" + punctaj +
                ", durataSauIncercari=" + durataSauIncercari + "}";
    }
}
