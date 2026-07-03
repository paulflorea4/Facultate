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

    public Configuratie() {}

    public Configuratie(String valori) {
        this.valori = valori;
    }

    public String getValori() {
        return valori;
    }

    public void setValori(String valori) {
        this.valori = valori;
    }

    @Override
    public String toString() {
        return "Configuratie{id=" + getId() + ", valori='" + valori + "'}";
    }
}
