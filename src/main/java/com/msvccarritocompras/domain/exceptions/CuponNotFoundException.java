package com.msvccarritocompras.domain.exceptions;

public class CuponNotFoundException extends RuntimeException{
    public CuponNotFoundException() {
    }

    public CuponNotFoundException(String message) {
        super(message);
    }
}
