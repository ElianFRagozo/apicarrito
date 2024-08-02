package com.msvccarritocompras.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;


public record CuponEditRequest(
        String descripcion,
        String codigo,
        String tipoDescuento,
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Float cantidad,
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$", message = "La fecha y hora deben tener el formato yyyy-MM-dd HH:mm")
        String fechaExpiracion
) {

}
