package org.example;

public class Persoana{
    protected String nume;
    protected byte varsta;
    public Persoana(){
        nume="Persoana";
        varsta=0;
    }
    public Persoana(String nume, byte varsta){
        this.nume=nume;
        this.varsta=varsta;
    }
    public void socializeaza() {
        System.out.println(nume + " socializeaza ... ");
    }

    @Override
    public String toString() {
        return "nume='" + nume + '\'' + ", varsta=" + varsta ;
    }

}
