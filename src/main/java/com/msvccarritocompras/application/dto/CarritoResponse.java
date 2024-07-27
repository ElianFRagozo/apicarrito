package com.msvccarritocompras.application.dto;

import com.msvccarritocompras.infrastructure.api.producto.dto.ProductoDto;

import java.util.List;

public record CarritoResponse(
        Long carritoId,
        Long usuarioId,
        Float total,
        String codigoCupon,
        List<CarritoResponse.ItemCarritoResponse> itemCarritoList
) {
    public record ItemCarritoResponse(
            Long itemCarritoId,
            Integer cantidad,
            Float precioUnitarioItem,
            ProductoDto productoDto
    ){}
}
