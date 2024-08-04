package com.msvccarritocompras.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String codigo;
    private String mensaje;
    private String error;
}
