package org.example.ducksocialnetworkui.flock;

import org.example.ducksocialnetworkui.domain.SwimmingDuck;
import org.example.ducksocialnetworkui.domain.TipRata;

public class SwimmingFlock extends Flock<SwimmingDuck> {
    public SwimmingFlock(Long id, String name) {
        super(id, name);
    }

    @Override
    public boolean canAccept(TipRata tip) {
        return tip==TipRata.SWIMMING;
    }
}