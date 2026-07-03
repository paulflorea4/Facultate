package org.example.laboratorGUI.domain.user.duck;

import org.example.laboratorGUI.domain.user.User;
import org.example.laboratorGUI.utils.types.TipRata;

public abstract class Duck extends User {
    private final TipRata type;
    private double speed;
    private double resistance;
    private Long flockID;

    public Duck(Long userID, String username, String email, String password,
                TipRata type, Double speed, Double resistance, Long flockID) {
        super(userID, username, email, password);
        this.type = type;
        this.speed = speed;
        this.resistance = resistance;
        this.flockID = flockID;
    }

    public TipRata getType() {
        return type;
    }

    public double getSpeed() {
        return speed;
    }

    public double getResistance() {
        return resistance;
    }

    public Long getFlockID() {
        return flockID;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }

    public void setFlockID(Long flockID) {
        this.flockID = flockID;
    }

    @Override
    public String toString() {
        return super.toString() + "\nDuck: tip = " + type + ", viteza = " + speed + ", rezistenta = " + resistance + ", card = " + flockID;
    }

}
