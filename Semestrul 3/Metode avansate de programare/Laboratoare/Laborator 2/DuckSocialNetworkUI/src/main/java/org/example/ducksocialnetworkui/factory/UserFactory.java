package org.example.ducksocialnetworkui.factory;

import org.example.ducksocialnetworkui.domain.*;

import java.time.LocalDate;

public class UserFactory implements Factory<User> {
    private UserFactory(){}

    private static UserFactory instance;

    public static UserFactory getInstance(){
        if(instance == null){
            instance = new UserFactory();
        }
        return instance;
    }

    @Override
    public User create(Object... params) {
        String type = params[0].toString().toLowerCase();

        Long id=(Long) params[1] ;           // id

        if (type.equals("persoana")) {
            return new Persoana(
                    id,
                    (String) params[2],           // username
                    (String) params[3],           // email
                    (String) params[4],           // password
                    (String) params[5],           // nume
                    (String) params[6],           // prenume
                    (LocalDate) params[7],        // data nasterii
                    (String) params[8],           // ocupatie
                    (Double) params[9]            // empatie
            );
        } else if (type.equals("duck") || type.equals("rata")) {
            TipRata tipRata = (TipRata) params[5];
            if(tipRata.equals(TipRata.SWIMMING)) {
                return new SwimmingDuck(
                        id,
                        (String) params[2],           // username
                        (String) params[3],           // email
                        (String) params[4],           // password
                        (Double) params[6],           // viteza
                        (Double) params[7],           // rezistenta
                        (Long) params[8]             // cardul
                );
            } else if (tipRata.equals(TipRata.FLYING)) {
                return new FlyingDuck(
                        id,
                        (String) params[2],          // username
                        (String) params[3],          // email
                        (String) params[4],          // password
                        (Double) params[6],          // viteza
                        (Double) params[7],          // rezistenta
                        (Long) params[8]              // cardul
                );
            }
            else {
                return new FlyingAndSwimmingDuck(
                        id,
                        (String) params[2],           // username
                        (String) params[3],           // email
                        (String) params[4],           // password
                        (Double) params[6],           // viteza
                        (Double) params[7],           // rezistenta
                        (Long) params[8]               //cardul
                );
            }
        }

        throw new IllegalArgumentException("Tip necunoscut de utilizator: " + type);
    }
}