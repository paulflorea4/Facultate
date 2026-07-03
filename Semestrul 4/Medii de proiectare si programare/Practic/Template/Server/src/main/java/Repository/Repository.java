package Repository;

import Domain.Entity;
import java.io.Serializable;

public interface Repository<ID extends Serializable, E extends Entity<ID>> {
    E findOne(ID id);
    Iterable<E> findAll();
    E save(E entity);
    E delete(ID id);
    E update(E entity);
    void close();
}
