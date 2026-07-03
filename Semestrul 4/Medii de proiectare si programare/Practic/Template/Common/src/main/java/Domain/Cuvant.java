package Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.io.Serializable;

@jakarta.persistence.Entity
@Table(name = "Cuvinte")
public class Cuvant extends Entity<Long> implements Serializable {
    private static final long serialVersionUID = 5L;

    @Column(name = "valoare")
    private String valoare;

    @Column(name = "indiciu")
    private String indiciu;

    @Column(name = "complexitate")
    private String complexitate;

    public Cuvant() {}

    public Cuvant(String valorare, String indiciu, String complexitate) {
        this.valoare = valorare;
        this.indiciu = indiciu;
        this.complexitate = complexitate;
    }

    public String getValoare() {
        return valoare;
    }

    public void setValoare(String valoare) {
        this.valoare = valoare;
    }

    public String getIndiciu() {
        return indiciu;
    }

    public void setIndiciu(String indiciu) {
        this.indiciu = indiciu;
    }

    public String getComplexitate() {
        return complexitate;
    }

    public void setComplexitate(String complexitate) {
        this.complexitate = complexitate;
    }
}
