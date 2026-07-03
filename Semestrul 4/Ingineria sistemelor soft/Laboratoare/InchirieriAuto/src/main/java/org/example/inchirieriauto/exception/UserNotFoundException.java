package org.example.inchirieriauto.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Utilizatorul autentificat nu a fost găsit.");
    }
}

