package org.example.ducksocialnetworkui.domain;

public class FlyingAndSwimmingDuck extends Duck implements Zburator, Inotator {

    public FlyingAndSwimmingDuck(Long id, String username, String email, String password,
                                 double viteza, double rezistenta, long cardId) {
        super(id, username, email, password, TipRata.FLYING_AND_SWIMMING, viteza, rezistenta,cardId);
    }

    @Override
    public void zboara() {
        System.out.println(getUsername() + " zboara cu viteza " + getViteza() + " m/s!");
    }

    @Override
    public void inoata() {
        System.out.println(getUsername() + " inoata cu rezistența " + getRezistenta() + "!");
    }

    @Override
    public String toString() {
        return super.toString();
    }
}