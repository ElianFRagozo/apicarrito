package com.msvccarritocompras.domain.exceptions;

public class InvalidCantidadException extends RuntimeException{
    public InvalidCantidadException() {
        super();
    }

    public InvalidCantidadException(String message) {
        super(message);
    }
}
