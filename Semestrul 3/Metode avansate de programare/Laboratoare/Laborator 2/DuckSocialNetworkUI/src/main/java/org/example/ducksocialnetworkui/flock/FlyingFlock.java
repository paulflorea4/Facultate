package org.example.ducksocialnetworkui.flock;

import org.example.ducksocialnetworkui.domain.FlyingDuck;
import org.example.ducksocialnetworkui.domain.TipRata;

public class FlyingFlock extends Flock<FlyingDuck> {
    public FlyingFlock(Long id, String name) {
        super(id, name);
    }

    @Override
    public boolean canAccept(TipRata tip) {
        return tip==TipRata.FLYING;
    }
}