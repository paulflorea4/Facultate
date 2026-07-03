package Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.io.Serializable;

@jakarta.persistence.Entity
@Table(name = "Jucatori")
public class Jucator extends Entity<Long> implements Serializable {
    private static final long serialVersionUID = 2L;

    @Column(name = "alias")
    private String alias;

    public Jucator() {}

    public Jucator(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "Jucator{id=" + getId() + ", alias='" + alias + "'}";
    }
}
