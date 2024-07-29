package com.msvccarritocompras.domain.exceptions;

public class CarritoNotFoundException extends RuntimeException{
    public CarritoNotFoundException() {
        super();
    }

    public CarritoNotFoundException(String message) {
        super(message);
    }
}
