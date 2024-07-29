package com.msvccarritocompras.application.dto;

import jakarta.validation.constraints.Min;


public record CuponEditRequest(
        String descripcion,
        String codigo,
        String tipoDescuento,
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Float cantidad
) {

}
