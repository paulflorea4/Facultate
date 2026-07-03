package org.example.ducksocialnetworkui.domain;

public class FlyingDuck extends Duck implements Zburator{
    public FlyingDuck(Long id, String username, String email, String password, double viteza, double rezistenta, long cardId) {
        super(id, username, email, password, TipRata.FLYING, viteza, rezistenta,cardId);
    }

    @Override
    public void zboara() {
        System.out.println(getUsername() + " zboara cu viteza " + getViteza() + " m/s!");
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
