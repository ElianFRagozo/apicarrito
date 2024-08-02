package com.msvccarritocompras.application.dto;

import com.msvccarritocompras.infrastructure.api.producto.dto.ProductoDto;
import com.msvccarritocompras.infrastructure.api.usuarios.dto.UsuarioDto;

import java.util.List;

public record CarritoResponse(
        Long carritoId,
        UsuarioDto usuarioDto,
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
