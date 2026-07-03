package org.example.inchirieriauto.exception;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(Integer carId) {
        super("Mașina cu ID-ul " + carId + " nu a fost găsită.");
    }
}

