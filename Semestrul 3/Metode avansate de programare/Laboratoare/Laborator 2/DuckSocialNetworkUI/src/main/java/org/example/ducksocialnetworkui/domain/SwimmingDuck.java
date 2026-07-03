package org.example.ducksocialnetworkui.domain;

public class SwimmingDuck extends Duck implements Inotator{
    public SwimmingDuck(Long id, String username, String email, String password, double viteza, double rezistenta, long cardId) {
        super(id, username, email, password, TipRata.SWIMMING, viteza, rezistenta,cardId);
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
