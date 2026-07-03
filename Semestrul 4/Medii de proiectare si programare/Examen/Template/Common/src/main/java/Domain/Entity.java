package Domain;

import jakarta.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public class Entity<ID extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    public Entity() {}

    public ID getId() { return id; }
    public void setId(ID id) { this.id = id; }
}
