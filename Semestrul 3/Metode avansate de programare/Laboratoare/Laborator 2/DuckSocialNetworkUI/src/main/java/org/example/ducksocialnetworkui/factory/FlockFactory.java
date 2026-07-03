package org.example.ducksocialnetworkui.factory;

import org.example.ducksocialnetworkui.domain.Duck;
import org.example.ducksocialnetworkui.domain.TipRata;
import org.example.ducksocialnetworkui.flock.Flock;
import org.example.ducksocialnetworkui.flock.FlyingFlock;
import org.example.ducksocialnetworkui.flock.HybridFlock;
import org.example.ducksocialnetworkui.flock.SwimmingFlock;

public class FlockFactory implements Factory<Flock<? extends Duck>>{
    private FlockFactory(){}

    private static FlockFactory instance;

    public static FlockFactory getInstance(){
        if(instance == null){
            instance = new FlockFactory();
        }
        return instance;
    }

    @Override
    public Flock<? extends Duck> create(Object... params) {
        TipRata tipRata = TipRata.valueOf(params[0].toString().toUpperCase());
        Long id=(Long) params[1];
        String name=(String) params[2];
        if(tipRata==TipRata.FLYING) return new FlyingFlock(id, name);
        if(tipRata==TipRata.SWIMMING)  return new SwimmingFlock(id, name);
        return new HybridFlock(id, name);
    }
}