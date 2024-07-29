package com.msvccarritocompras.domain.exceptions;

public class InvalidTipoDescuentoException extends RuntimeException {
    public InvalidTipoDescuentoException(String message) {
        super(message);
    }
}
