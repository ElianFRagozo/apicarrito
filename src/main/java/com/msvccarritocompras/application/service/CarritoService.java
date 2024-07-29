package com.msvccarritocompras.application.service;

import com.msvccarritocompras.application.dto.CarritoRequest;
import com.msvccarritocompras.application.dto.CarritoResponse;
import com.msvccarritocompras.application.repository.CarritoRepository;
import com.msvccarritocompras.domain.entity.Carrito;
import com.msvccarritocompras.domain.entity.Cupon;
import com.msvccarritocompras.domain.entity.ItemCarrito;
import com.msvccarritocompras.domain.exceptions.CarritoNotFoundException;
import com.msvccarritocompras.infrastructure.api.producto.client.ProductoFeingClient;
import com.msvccarritocompras.infrastructure.api.producto.dto.ProductoDto;
import com.msvccarritocompras.infrastructure.persistence.CarritoPersistence;
import com.msvccarritocompras.infrastructure.persistence.CuponPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarritoService implements CarritoRepository {

    private final CarritoPersistence carritoPersistence;
    private final ProductoFeingClient productoFeingClient;
    private final CuponPersistence cuponPersistence;


    @Override
    public CarritoResponse crearCarrito(CarritoRequest carritoRequest) {
        String codigoCupon = carritoRequest.codigoCupon() != null ? carritoRequest.codigoCupon() : "no-cupon";
        Cupon cuponEncontrado = cuponPersistence.findCuponByCodigo(codigoCupon);

        List<ItemCarrito> itemCarritoList = carritoRequest.itemcarritoList().stream()
                .map(itemCarritoRequest -> {
                    ProductoDto productoDto = productoFeingClient.getProductById(itemCarritoRequest.productoId());
                    BigDecimal precioUnitarioItem = BigDecimal.valueOf(productoDto.getPrice() * itemCarritoRequest.cantidad())
                            .setScale(2, RoundingMode.HALF_UP);
                    return ItemCarrito.builder()
                            .productoId(productoDto.getId())
                            .cantidad(itemCarritoRequest.cantidad())
                            .precioUnitarioItem(precioUnitarioItem.floatValue())
                            .build();
                })
                .collect(Collectors.toList());

        BigDecimal descuento = BigDecimal.valueOf(cuponEncontrado.getCantidad() / 100.0);
        BigDecimal totalCarrito = itemCarritoList.stream()
                .map(itemCarrito -> BigDecimal.valueOf(itemCarrito.getPrecioUnitarioItem()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalConDescuento = totalCarrito.multiply(BigDecimal.ONE.subtract(descuento))
                .setScale(2, RoundingMode.HALF_UP);

        Carrito carrito = Carrito.builder()
                .usuarioId(carritoRequest.usuarioId())
                .total(totalConDescuento.floatValue())
                .cupon(cuponEncontrado)
                .itemCarritoList(itemCarritoList)
                .build();

        Carrito carritoGuardado = carritoPersistence.save(carrito);

        List<CarritoResponse.ItemCarritoResponse> itemCarritoResponseList = carritoGuardado.getItemCarritoList().stream()
                .map(itemCarrito -> {
                    ProductoDto productoDto = productoFeingClient.getProductById(itemCarrito.getProductoId());

                    return new CarritoResponse.ItemCarritoResponse(
                            itemCarrito.getItemCarritoId(),
                            itemCarrito.getCantidad(),
                            itemCarrito.getPrecioUnitarioItem(),
                            productoDto
                    );
                })
                .collect(Collectors.toList());

        return new CarritoResponse(
                carritoGuardado.getCarritoId(),
                carritoGuardado.getUsuarioId(),
                carritoGuardado.getTotal(),
                carritoGuardado.getCupon().getCodigo(),
                itemCarritoResponseList
        );
    }

    @Override
    public CarritoResponse getCarrito(Long carritoId) {
        Carrito carritoBd = carritoPersistence.findById(carritoId)
                .orElseThrow(()-> new CarritoNotFoundException("carrito con el id:"+carritoId+" no encontrado"));

        List<CarritoResponse.ItemCarritoResponse> itemCarritoResponseList= carritoBd.getItemCarritoList().stream().map(itemCarrito -> {
                    ProductoDto productoDto = productoFeingClient.getProductById(itemCarrito.getProductoId());

                   return new CarritoResponse.ItemCarritoResponse(
                    itemCarrito.getItemCarritoId(),
                    itemCarrito.getCantidad(),
                    itemCarrito.getPrecioUnitarioItem(),
                            productoDto );
        }).toList();

        return new CarritoResponse(
                carritoBd.getCarritoId(),
                carritoBd.getUsuarioId(),
                carritoBd.getTotal(),
                carritoBd.getCupon().getCodigo(),
                itemCarritoResponseList
        );
    }

    @Override
    public CarritoResponse editCarrito(Long carritoId, CarritoRequest carritoRequest) {
        return null;
    }

    @Override
    public void deleteCarrito(Long carritoId) {

    }
}
