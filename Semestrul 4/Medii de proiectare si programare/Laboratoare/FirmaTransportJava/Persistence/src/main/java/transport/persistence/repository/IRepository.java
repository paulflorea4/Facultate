package transport.persistence.repository;

import transport.model.Entity;

import java.util.List;

public interface IRepository<ID, E extends Entity<ID>> {
    ID add(E entity);
    void delete(ID id);
    void update(E entity);
    E findById(ID id);
    List<E> findAll();
}
