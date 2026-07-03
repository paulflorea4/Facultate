package org.example.laboratorGUI.repository.memory;

import org.example.laboratorGUI.domain.Entity;
import org.example.laboratorGUI.exceptions.RepositoryException;
import org.example.laboratorGUI.repository.Repository;

import java.util.*;

public class InMemoryRepository<ID, TElem extends Entity<ID>> implements Repository<ID, TElem> {

    protected final Map<ID, TElem> repo = new HashMap<>();

    /**
     * Saves a new entity in the repository.
     * If an entity with the same ID already exists, the entity is not added.
     *
     * @param entity the entity to save
     * @throws RepositoryException if the entity is null
     */
    public void save(TElem entity) {
//        if (entity == null) {
//            throw new RepositoryException("Entity can not be null!");
//        }
//        ID id = entity.getId();
//        if (!repo.containsKey(id)) {
//            repo.put(id, entity);
//        }
        throw new UnsupportedOperationException("This operation is not longer supported!");
    }

    /**
     * Finds an entity by its ID.
     *
     * @param id the ID of the entity to find
     * @return the entity with the given ID, or null if it does not exist
     * @throws RepositoryException if the ID is null
     */
    public TElem findById(ID id) {
        if (id == null) {
            throw new RepositoryException("ID can not be null!");
        }
        return repo.get(id);
    }

    /**
     * Returns a list of all entities stored in the repository.
     *
     * @return a list containing all entities
     */
    public List<TElem> findAll() {
        return new ArrayList<>(repo.values());
    }

    /**
     * Deletes the entity with the specified ID from the repository.
     * If the ID does not exist, nothing happens.
     *
     * @param id the ID of the entity to delete
     * @throws RepositoryException if the ID is null
     */
    public void delete(ID id) {
        if (id == null) {
            throw new RepositoryException("ID can not be null!");
        }
        repo.remove(id);
    }

    /**
     * Updates an existing entity in the repository.
     * If the entity does not exist, the method returns the entity.
     *
     * @param entity the entity to update
     * @throws RepositoryException if the entity is null
     */
    public void update(TElem entity) {
//        if (entity == null) {
//            throw new RepositoryException("Entity can not be null!");
//        }
//        ID id = entity.getId();
//        if (!repo.containsKey(id)) {
//            return;
//        }
//        repo.put(id, entity);
        throw new UnsupportedOperationException("This operation is not longer supported!");
    }
}
