package org.example;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<String> lista = Arrays.asList("bbb", "ana", "ccc", "alex");
        lista.stream()
                .peek(x -> System.out.println("START: " + x))
                .filter(x -> {
                    System.out.println("FILTER: " + x);
                    return x.startsWith("a");
                }
                .map(x -> {
                    System.out.println("MAP: " + x);
                    return x.toUpperCase();
                });

    }
}



class A {
    static { System.out.print("S"); }
    { System.out.print("I"); }
    A() { System.out.print("A"); }
}

class B extends A {
    static { System.out.print("T"); }
    { System.out.print("J"); }
    B() { System.out.print("B"); }
}


class Box<T> {
    public void getValue() {}
    public static Double suma(List<? extends Number> nr){
        return nr.stream().mapToDouble(Number::doubleValue).sum();
    }
}

interface Formula2{
    double calculate(double v1, double v2);
}

class VehicleService {

    public static List<Vehicle> addVehicle(
            List<Vehicle> vehicles,
            Vehicle v,
            int position) {
        vehicles.add(position, v);
        return vehicles;
    }
}


class Vehicle{
    private Integer VIN;
    private Double price;
    private String brand;

    public Vehicle(Integer VIN,Double price,String brand) {
        this.VIN=VIN;
        this.price=price;
        this.brand=brand;
    }

    public Integer getVIN() { return VIN; }
    public Double getPrice() { return price; }
    public String getBrand() { return brand; }

    @Override
    public String toString(){
        return VIN + " " + price + " " + brand;
    }
}


class Contact {
    public static String Name = "Mapescu I ";

    class Numar1 {
        private String nr = "0987654321";
        public String ContactNou() {
            return Name + nr;
        }
    }

    static class Numar2 {
        private String nr = "0945654321";
        public String ContactNou() {
            return Name + nr;
        }
    }
}

interface Formula {
    double calculate(double a);
}

class EmailSender {
    private String message;

    public EmailSender(String s) {
        message = s;
    }

    public String run() {
        System.out.print(message + " ");
        return "done";
    }
}

class D implements Formula {
    static int var1 = 100;
    double x = 9;

    public double calculate(double a) {
        double x = var1 * a; // (1)         // (2)

        Formula f = (double b) -> {
            return Math.abs(x);    // (3)
        };

        return f.calculate(a);     // (4)
    }
}

class Complex {
    private int real;
    private int imaginar;
    Complex(int real,int imaginar) {
        this.real = real;
        this.imaginar = imaginar;
    }

    public void aduna(int real,int imaginar){
        this.real+=real;
        this.imaginar+=imaginar;
    }

    public void aduna(Complex c){
        this.real+=c.getReal();
        this.imaginar+=c.getImaginar();
    }

    public int getReal(){ return this.real; }
    public int getImaginar() { return this.imaginar; }
}




