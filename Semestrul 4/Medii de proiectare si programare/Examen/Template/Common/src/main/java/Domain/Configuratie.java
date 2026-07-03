package Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.io.Serializable;

@jakarta.persistence.Entity
@Table(name = "Configuratii")
public class Configuratie extends Entity<Long> implements Serializable {
    private static final long serialVersionUID = 3L;

    @Column(name = "valori")
    private String valori;

    @Column(name = "n")
    private int n;

    public Configuratie() {}

    public Configuratie(String valori, int n) {
        this.valori = valori;
        this.n = n;
    }

    public String getValori() {
        return valori;
    }

    public void setValori(String valori) {
        this.valori = valori;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return "Configuratie{id=" + getId() + ", valori='" + valori + "', n=" + n + "}";
    }
}
