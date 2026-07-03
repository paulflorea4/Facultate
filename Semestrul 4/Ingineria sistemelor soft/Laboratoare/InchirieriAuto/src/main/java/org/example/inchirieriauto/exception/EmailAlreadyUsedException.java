package org.example.inchirieriauto.exception;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException() {
        super("Există deja un cont cu emailul introdus");
    }
}

