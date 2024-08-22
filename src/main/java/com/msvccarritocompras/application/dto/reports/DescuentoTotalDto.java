package com.msvccarritocompras.application.dto.reports;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class DescuentoTotalDto {
    private BigDecimal total;
    private Float descuentoPorcentaje;
}
