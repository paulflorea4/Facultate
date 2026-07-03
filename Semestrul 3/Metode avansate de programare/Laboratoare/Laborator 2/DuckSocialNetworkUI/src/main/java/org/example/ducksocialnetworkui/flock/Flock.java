package org.example.ducksocialnetworkui.flock;

import org.example.ducksocialnetworkui.domain.Duck;
import org.example.ducksocialnetworkui.domain.Entity;
import org.example.ducksocialnetworkui.domain.TipRata;

public abstract class Flock<T extends Duck> extends Entity<Long>{
    private final String name;

    public Flock(Long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract boolean canAccept(TipRata tip);

    @Override
    public String toString() {
        return "Card{" +
                "id=" + getId() +
                ", numeCard='" + name +
                '}';
    }
}
