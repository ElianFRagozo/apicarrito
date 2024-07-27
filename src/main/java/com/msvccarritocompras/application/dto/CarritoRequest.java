package com.msvccarritocompras.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CarritoRequest(
        String codigoCupon,
        @NotNull(message = "el campo usuarioId no debe estar vacio !!")
        Long usuarioId,
        @Valid @NotEmpty(message = "el item Carrito no debe estar vacio")
        List<ItemCarritoRequest> itemcarritoList ) {

public record ItemCarritoRequest(
        @NotNull(message = "el campo productoId no debe estar vacio !!")
        Long productoId,
        @NotNull(message = "La cantidad no puede ser nula")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Integer cantidad
) {}
}