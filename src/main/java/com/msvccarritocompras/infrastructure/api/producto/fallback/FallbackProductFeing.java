package com.msvccarritocompras.infrastructure.api.producto.fallback;

import com.msvccarritocompras.infrastructure.api.producto.client.ProductoFeingClient;
import com.msvccarritocompras.infrastructure.api.producto.dto.ProductoDto;

public class FallbackProductFeing implements ProductoFeingClient {
    @Override
    public ProductoDto getProductById(Long productoId) {
        ProductoDto productoDto=new ProductoDto();
        productoDto.setId(productoId);
        productoDto.setPrice(0.0f);
        return  productoDto;
    }
}
