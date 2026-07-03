package org.example.ducksocialnetworkui.flock;

import org.example.ducksocialnetworkui.domain.FlyingAndSwimmingDuck;
import org.example.ducksocialnetworkui.domain.TipRata;

public class HybridFlock extends Flock<FlyingAndSwimmingDuck> {
    public HybridFlock(Long id, String name) {
        super(id, name);
    }

    @Override
    public boolean canAccept(TipRata tip) {
        return tip==TipRata.FLYING_AND_SWIMMING;
    }
}
