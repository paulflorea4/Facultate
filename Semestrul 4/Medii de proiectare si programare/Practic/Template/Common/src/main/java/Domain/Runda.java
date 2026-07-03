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

    @Column(name = "cuvant_server")
    private String cuvantServer;

    @Column(name = "cuvant_jucator")
    private String cuvantJucator;

    @Column(name = "punctaj")
    private int punctaj;

    public Runda() {}

    public Runda(Jucator jucator, Joc joc, String cuvantServer, String cuvantJucator, int punctaj) {
        this.jucator = jucator;
        this.joc = joc;
        this.cuvantServer = cuvantServer;
        this.cuvantJucator = cuvantJucator;
        this.punctaj = punctaj;
    }

    public Jucator getJucator() { return jucator; }
    public void setJucator(Jucator jucator) { this.jucator = jucator; }

    public Joc getJoc() { return joc; }
    public void setJoc(Joc joc) { this.joc = joc; }

    public int getPunctaj() { return punctaj; }
    public void setPunctaj(int punctaj) { this.punctaj = punctaj; }

    public String getCuvantServer() {
        return cuvantServer;
    }

    public void setCuvantServer(String cuvantServer) {
        this.cuvantServer = cuvantServer;
    }

    public String getCuvantJucator() {
        return cuvantJucator;
    }

    public void setCuvantJucator(String cuvantJucator) {
        this.cuvantJucator = cuvantJucator;
    }
}
