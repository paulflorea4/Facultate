package org.example.laboratorGUI.service;

import org.example.laboratorGUI.domain.flock.*;
import org.example.laboratorGUI.domain.flock.Flock;
import org.example.laboratorGUI.domain.flock.FlyingFlock;
import org.example.laboratorGUI.domain.user.duck.Duck;
import org.example.laboratorGUI.domain.user.duck.FlyingDuck;
import org.example.laboratorGUI.domain.user.duck.FlyingSwimmingDuck;
import org.example.laboratorGUI.domain.user.duck.SwimmingDuck;
import org.example.laboratorGUI.exceptions.ServiceException;
import org.example.laboratorGUI.factory.FlockFactory;
import org.example.laboratorGUI.repository.database.DBFlockRepository;
import org.example.laboratorGUI.repository.database.DBUserRepository;
import org.example.laboratorGUI.utils.types.TipRata;
import org.example.laboratorGUI.validators.FlockValidator;
import org.example.laboratorGUI.validators.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FlockService {
    private final DBUserRepository userRepo;
    private final DBFlockRepository flockRepo;

    private final Validator<Flock<? extends Duck>> flockValidator = new FlockValidator();

    public FlockService(DBUserRepository userRepo, DBFlockRepository flockRepo) {
        this.userRepo = userRepo;
        this.flockRepo = flockRepo;
    }

    /**
     * Adds a new flock to the network.
     *
     * @param id the unique id of the flock
     * @param name the name of the flock
     * @param tip the type of the flock
     */
    public void addflock(Long id, String name, TipRata tip){
        Flock<? extends Duck> flock = FlockFactory.createFlock(id, name, tip);
        flockValidator.validate(flock);
        flockRepo.save(flock);
    }

    /**
     * Removes a flock from the network
     *
     * @param id the unique id of the flock
     */
    public void removeflock(Long id) {
        if (flockRepo.findById(id) == null)
            throw new ServiceException("Flock not found");
        flockRepo.delete(id);
    }

    /**
     * Returns a list of all flocks in the network
     *
     * @return list of all flocks
     */
    public List<Flock<? extends Duck>> getAllFlocks() {
        Map<Long, Flock<? extends Duck>> flocks = flockRepo.findAll().stream()
                .collect(Collectors.toMap(Flock::getFlockID, c -> c));

        for (Duck duck : userRepo.findAllDucks()) {
            Flock<? extends Duck> flock = flocks.get(duck.getFlockID());
            if (flock == null)
                continue;
            switch (duck.getType()) {
                case FLYING ->
                        ((FlyingFlock) flock).addDuck((FlyingDuck) duck);
                case SWIMMING ->
                        ((SwimmingFlock) flock).addDuck((SwimmingDuck) duck);
                case FLYING_AND_SWIMMING ->
                        ((FlyingSwimmingFlock) flock).addDuck((FlyingSwimmingDuck) duck);
            }
        }
        return new ArrayList<>(flocks.values());
    }
}
