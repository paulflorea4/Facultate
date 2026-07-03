package org.example.laboratorGUI.factory;

import org.example.laboratorGUI.domain.flock.*;
import org.example.laboratorGUI.domain.flock.FlyingSwimmingFlock;
import org.example.laboratorGUI.domain.flock.SwimmingFlock;
import org.example.laboratorGUI.utils.types.TipRata;

public class FlockFactory {
    public static Flock<?> createFlock(Long id, String name, TipRata type){
        if (type == TipRata.FLYING)
            return new FlyingFlock(id, name, type);
        else if (type == TipRata.SWIMMING)
            return new SwimmingFlock(id, name, type);
        else
            return new FlyingSwimmingFlock(id, name, type);
    }
}
