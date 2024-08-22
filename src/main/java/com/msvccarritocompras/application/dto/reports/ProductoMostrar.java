package com.msvccarritocompras.application.dto.reports;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class ProductoMostrar {
    private String productName;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal precioTotalUnidad;
}
