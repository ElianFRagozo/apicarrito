package com.msvccarritocompras.application.repository;

import com.msvccarritocompras.application.dto.CarritoRequest;
import com.msvccarritocompras.application.dto.CarritoResponse;
import com.msvccarritocompras.domain.entity.Carrito;
import com.msvccarritocompras.domain.entity.Imagen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarritoRepository {


    CarritoResponse crearCarrito(CarritoRequest carritoRequest);
    CarritoResponse getCarrito(Long carritoId);
    CarritoResponse editCarrito(Long carritoId, CarritoRequest carritoRequest);
    void deleteCarrito(Long carritoId);

    Page<Carrito> listCarritoPaginado(Long usuarioId, Pageable pageable);
    List<Imagen> getUrlFactura(Long carritoId);
}