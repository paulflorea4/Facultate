package org.example;

interface MyInterface {
    int X = 10;
    int Y=15;
    int Z = 20;
    default int x() {
        return 0;
    }
    void foo();
    int f(int x);
}

