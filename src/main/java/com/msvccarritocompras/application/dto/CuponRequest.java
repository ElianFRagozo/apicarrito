package com.msvccarritocompras.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CuponRequest(
        @NotBlank(message = "el campo descripcion no debe ser nulo")
        String descripcion,
        @NotBlank(message = "el campo codigo no debe ser nulo")
        String codigo,
        @NotBlank(message = "el campo codigo no debe ser nulo")
        String tipoDescuento,
        @NotNull(message = "La cantidad no puede ser nula")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Float cantidad
) {
}
