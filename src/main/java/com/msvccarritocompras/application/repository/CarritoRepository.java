package com.msvccarritocompras.application.repository;

import com.msvccarritocompras.application.dto.CarritoRequest;
import com.msvccarritocompras.application.dto.CarritoResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoRepository {


    CarritoResponse crearCarrito(CarritoRequest carritoRequest);
    CarritoResponse getCarrito(Long carritoId);
    CarritoResponse editCarrito(Long carritoId, CarritoRequest carritoRequest);
    void deleteCarrito(Long carritoId);
}