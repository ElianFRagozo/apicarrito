package com.msvccarritocompras.application.dto;

import com.msvccarritocompras.infrastructure.api.producto.dto.ProductoDto;
import com.msvccarritocompras.infrastructure.api.usuarios.dto.UsuarioDto;

import java.util.List;

public record CarritoResponse(
        Long carritoId,
        UsuarioDto usuarioDto,
        Float total,
        String codigoCupon,
        List<ItemCarritoResponse> itemCarritoList,
        List<ImagenFacturas> imagenFacturasList
) {
    public record ItemCarritoResponse(
            Long itemCarritoId,
            Integer cantidad,
            Float precioUnitarioItem,
            ProductoDto productoDto
    ) {}

    public record ImagenFacturas(
            String urlImagen
    ) {}
}

