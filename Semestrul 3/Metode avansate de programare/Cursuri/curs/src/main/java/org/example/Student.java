package org.example;

public class Student extends Persoana {
    private int anStudiu;
    public Student(String nume, byte varsta, int anStudiu) {
        super(nume, varsta); //apel constr cu param din clasa de baza
        this.anStudiu = anStudiu;
    }

    public Student() {
        super();
        anStudiu=0;
    }

    public void seDistreaza() {
        super.socializeaza(); //apel metoda din clasa de baza
        System.out.println("Si canta ....");
    }

    @Override
    public String toString() {
        return super.toString()+", "+ "an de studiu="+anStudiu;
    }
}

