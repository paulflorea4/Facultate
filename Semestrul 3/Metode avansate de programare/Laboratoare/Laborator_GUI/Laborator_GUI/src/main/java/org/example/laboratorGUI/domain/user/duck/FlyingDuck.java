package org.example.laboratorGUI.domain.user.duck;

import org.example.laboratorGUI.utils.types.TipRata;

public class FlyingDuck extends Duck{
    public FlyingDuck(Long id, String username, String email, String password, TipRata type, Double speed, Double resistance, Long flockID) {
        super(id, username, email, password, type, speed, resistance, flockID);
    }

    @Override
    public String toString() {
        return super.toString() + "\nDuck is flying!";
    }
}
