package com.msvccarritocompras.domain.exceptions;

public class CuponExpiredException extends RuntimeException{
    public CuponExpiredException() {
        super();
    }

    public CuponExpiredException(String message) {
        super(message);
    }
}
