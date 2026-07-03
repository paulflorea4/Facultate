package org.example.laboratorGUI.repository.memory;

import org.example.laboratorGUI.domain.flock.Flock;
import org.example.laboratorGUI.domain.user.duck.Duck;
import org.example.laboratorGUI.exceptions.RepositoryException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlockRepository {
    protected final Map<Long, Flock<? extends Duck>> repo = new HashMap<>();

    /**
     * Saves a new flock in the repository.
     *
     * @param flock the flock to save
     */
    public void save(Flock<? extends Duck> flock) {
        if (flock == null)
            throw new RepositoryException("Flock can not be null!");
        if (repo.containsValue(flock))
            throw new RepositoryException("Flock already exists!");
        repo.put(flock.getFlockID(), flock);
    }

    /**
     * Deletes a flock from the repository
     *
     * @param id the id of the flock
     */
    public void delete(long id){
        repo.remove(id);
    }

    /**
     * Finds a flock by its id.
     *
     * @param id the id of the flock to find
     * @return the flock with the given id
     */
    public Flock<? extends Duck> findById(long id) {
        return repo.get(id);
    }

    /**
     * Returns a list of all flock stored in the repository.
     *
     * @return a list containing all flocks
     */
    public List<Flock<? extends Duck>> findAll() {
        return new ArrayList<>(repo.values());
    }
}
