package org.example.laboratorGUI.domain.user.person;

import org.example.laboratorGUI.domain.user.User;

import java.time.LocalDate;

public class Person extends User {
    private String surname, name, job;
    private LocalDate dob;
    private double empathy;

    @Override
    public String toString() {
        return super.toString() + "\nPersoana: nume = " + surname + ", prenume = " + name +
                ", ocupatie = " + job + ", data nasterii = " + dob + ", nivelEmpatie = " + empathy;
    }

    public Person(Long userID, String username, String email, String password,
                  String surname, String name, String job, LocalDate dob, Double empathy) {
        super(userID, username, email, password);
        this.surname = surname;
        this.name = name;
        this.job = job;
        this.dob = dob;
        this.empathy = empathy;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public LocalDate getDob() {
        return dob;
    }

    public double getEmpathy() {
        return empathy;
    }

    public String getType(){
        return "Person";
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setEmpathy(double empathy) {
        this.empathy = empathy;
    }
}
