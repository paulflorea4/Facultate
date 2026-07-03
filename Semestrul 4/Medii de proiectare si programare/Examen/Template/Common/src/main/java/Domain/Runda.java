package Domain;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.io.Serializable;

@jakarta.persistence.Entity
@Table(name = "Runde")
public class Runda extends Entity<Long> implements Serializable {
    private static final long serialVersionUID = 5L;

    @ManyToOne
    @JoinColumn(name = "jucator_id")
    private Jucator jucator;

    @ManyToOne
    @JoinColumn(name = "joc_id")
    private Joc joc;

    @Column(name = "pozitie")
    private String pozitie;

    @Column(name = "punctaj")
    private int punctaj;

    public Runda() {}

    public Runda(Jucator jucator, Joc joc, String pozitie, int punctaj) {
        this.jucator = jucator;
        this.joc = joc;
        this.pozitie = pozitie;
        this.punctaj = punctaj;
    }

    public Jucator getJucator() { return jucator; }
    public void setJucator(Jucator jucator) { this.jucator = jucator; }

    public Joc getJoc() { return joc; }
    public void setJoc(Joc joc) { this.joc = joc; }

    public String getPozitie() { return pozitie; }
    public void setPozitie(String pozitie) { this.pozitie = pozitie; }

    public int getPunctaj() { return punctaj; }
    public void setPunctaj(int punctaj) { this.punctaj = punctaj; }
}
