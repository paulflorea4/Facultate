package org.example.template.domain;

public class Car extends Entity<Long>{
    private String denumire;
    private String descriere;
    private Integer pret;
    private Status status;

    public Car(Long aLong, String denumire, String descriere, Integer pret, Status status) {
        super(aLong);
        this.denumire = denumire;
        this.descriere = descriere;
        this.pret = pret;
        this.status = status;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public Integer getPret() {
        return pret;
    }

    public void setPret(Integer pret) {
        this.pret = pret;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return denumire + " | " + descriere + " | " + pret + " | " + status;
    }
}
