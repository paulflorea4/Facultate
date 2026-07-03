package org.example.laboratorGUI.domain.flock;

import org.example.laboratorGUI.domain.user.duck.SwimmingDuck;
import org.example.laboratorGUI.utils.types.TipRata;

public class SwimmingFlock extends Flock<SwimmingDuck> {
    public SwimmingFlock(Long id, String name, TipRata type) {
        super(id, name, type);
    }

    public boolean canAccept(TipRata duckType) {
        return duckType == getType();
    }
}
