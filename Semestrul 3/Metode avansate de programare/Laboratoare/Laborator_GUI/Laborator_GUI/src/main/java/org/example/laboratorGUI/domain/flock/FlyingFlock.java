package org.example.laboratorGUI.domain.flock;

import org.example.laboratorGUI.domain.user.duck.FlyingDuck;
import org.example.laboratorGUI.utils.types.TipRata;

public class FlyingFlock extends Flock<FlyingDuck> {
    public FlyingFlock(Long id, String name, TipRata type) {
        super(id, name, type);
    }

    public boolean canAccept(TipRata duckType) {
        return duckType == getType();
    }
}
