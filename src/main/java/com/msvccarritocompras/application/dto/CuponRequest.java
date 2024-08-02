package com.msvccarritocompras.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


public record CuponRequest(
        @NotBlank(message = "el campo descripcion no debe ser nulo")
        String descripcion,
        @NotBlank(message = "el campo codigo no debe ser nulo")
        String codigo,
        @NotBlank(message = "el campo codigo no debe ser nulo")
        String tipoDescuento,
        @NotNull(message = "La cantidad no puede ser nula")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Float cantidad,
        @NotBlank(message = "el campo tiempo Expiracion no debe ser nulo")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$", message = "La fecha y hora deben tener el formato yyyy-MM-dd HH:mm")
        String fechaExpiracion
) {
}
