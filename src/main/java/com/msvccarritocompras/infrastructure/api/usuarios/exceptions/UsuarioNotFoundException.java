package com.msvccarritocompras.infrastructure.api.usuarios.exceptions;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(Long usuarioId) {
        super("Usuario no encontrado con el ID: " + usuarioId);
    }
}