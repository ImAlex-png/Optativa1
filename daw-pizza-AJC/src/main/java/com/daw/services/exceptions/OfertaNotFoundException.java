package com.daw.services.exceptions;

public class OfertaNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OfertaNotFoundException(String message) {
        super(message);
    }

}
