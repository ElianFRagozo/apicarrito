package com.msvccarritocompras.domain.exceptions;

public class CuponCodigoExistenteException extends RuntimeException{
    public CuponCodigoExistenteException() {
        super();
    }

    public CuponCodigoExistenteException(String message) {
        super(message);
    }
}
