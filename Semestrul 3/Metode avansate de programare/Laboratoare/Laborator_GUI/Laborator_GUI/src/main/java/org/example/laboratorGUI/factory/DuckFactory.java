package org.example.laboratorGUI.factory;

import org.example.laboratorGUI.domain.user.duck.Duck;
import org.example.laboratorGUI.domain.user.duck.FlyingDuck;
import org.example.laboratorGUI.domain.user.duck.FlyingSwimmingDuck;
import org.example.laboratorGUI.domain.user.duck.SwimmingDuck;
import org.example.laboratorGUI.utils.types.TipRata;

public class DuckFactory {
    public static Duck createDuck(Long id, String username, String email, String password,
                                  TipRata type, Double speed, Double resistance, Long flockID) {
        if (type == TipRata.FLYING)
            return new FlyingDuck(id, username, email, password, type, speed, resistance, flockID);
        else if (type == TipRata.SWIMMING)
            return new SwimmingDuck(id, username, email, password, type, speed, resistance, flockID);
        else
            return new FlyingSwimmingDuck(id, username, email, password, type, speed, resistance, flockID);
    }
}
