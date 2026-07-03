package org.example.laboratorGUI.domain.flock;

import org.example.laboratorGUI.domain.user.duck.FlyingSwimmingDuck;
import org.example.laboratorGUI.utils.types.TipRata;

public class FlyingSwimmingFlock extends Flock<FlyingSwimmingDuck> {
    public FlyingSwimmingFlock(Long id, String name, TipRata type) {
        super(id, name, type);
    }

    public boolean canAccept(TipRata duckType) {
        return duckType == getType();
    }
}
