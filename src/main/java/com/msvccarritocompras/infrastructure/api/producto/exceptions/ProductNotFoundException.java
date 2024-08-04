package com.msvccarritocompras.infrastructure.api.producto.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long productId) {
        super("Producto no encontrado con el ID: " + productId);
    }
}