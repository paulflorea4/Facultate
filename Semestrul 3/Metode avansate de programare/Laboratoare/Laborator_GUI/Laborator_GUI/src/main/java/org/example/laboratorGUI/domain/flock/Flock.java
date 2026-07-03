package org.example.laboratorGUI.domain.flock;

import org.example.laboratorGUI.domain.Entity;
import org.example.laboratorGUI.domain.user.duck.Duck;
import org.example.laboratorGUI.utils.types.TipRata;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Flock<TElem extends Duck> extends Entity<Long> {
    private final String name;
    private final TipRata type;
    private final List<TElem> members;

    public Flock(Long id, String name, TipRata type) {
        super(id);
        this.name = name;
        this.type = type;
        members = new ArrayList<>();
    }

    public Long getFlockID() {
        return super.getId();
    }

    public String getName() {
        return name;
    }

    public TipRata getType() {
        return type;
    }

    public abstract boolean canAccept(TipRata tipRata);

    public void addDuck(TElem duck) {
        if (!members.contains(duck)) {
            members.add(duck);
        }
    }

    public void removeDuck(TElem duck) {
        members.remove(duck);
    }

    public double getAverageSpeed() {
        double medieV = 0;
        for (Duck duck : members)
            medieV += duck.getSpeed();
        return medieV / members.size();
    }

    public double getAverageResistance() {
        double medieR = 0;
        for (Duck duck : members)
            medieR += duck.getResistance();
        return medieR / members.size();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Flock<?> flock = (Flock<?>) o;
        return Objects.equals(super.getId(), flock.getId()) && Objects.equals(name, flock.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId(), name);
    }

    @Override
    public String toString() {
        return "Flock: " + "id = " + super.getId() + ", nume = " + name + ", membri = " + members.size() +
                "\nViteza medie = " + getAverageSpeed() + ", rezistenta medie = " + getAverageResistance();
    }
}
