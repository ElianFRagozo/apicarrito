package com.msvccarritocompras.application.service;

import com.msvccarritocompras.application.dto.CarritoRequest;
import com.msvccarritocompras.application.dto.CarritoResponse;
import com.msvccarritocompras.application.repository.CarritoRepository;
import com.msvccarritocompras.domain.entity.Carrito;
import com.msvccarritocompras.domain.entity.Cupon;
import com.msvccarritocompras.domain.entity.ItemCarrito;
import com.msvccarritocompras.domain.enums.TipoDescuento;
import com.msvccarritocompras.domain.exceptions.CarritoNotFoundException;
import com.msvccarritocompras.domain.exceptions.CuponExpiredException;
import com.msvccarritocompras.domain.exceptions.CuponNotFoundException;
import com.msvccarritocompras.infrastructure.api.producto.client.ProductoFeingClient;
import com.msvccarritocompras.infrastructure.api.producto.dto.ProductoDto;
import com.msvccarritocompras.infrastructure.api.producto.exceptions.ProductNotFoundException;
import com.msvccarritocompras.infrastructure.api.usuarios.client.UsuarioFeingClient;
import com.msvccarritocompras.infrastructure.api.usuarios.dto.UsuarioDto;
import com.msvccarritocompras.infrastructure.persistence.repository.CarritoPersistence;
import com.msvccarritocompras.infrastructure.persistence.repository.CuponPersistence;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarritoService implements CarritoRepository {

    private final CarritoPersistence carritoPersistence;
    private final ProductoFeingClient productoFeingClient;
    private final CuponPersistence cuponPersistence;
    private final UsuarioFeingClient usuarioFeingClient;


    @Override
    public CarritoResponse crearCarrito(CarritoRequest carritoRequest) {
        BigDecimal totalConDescuento;
        String codigoCupon = carritoRequest.codigoCupon() != null ? carritoRequest.codigoCupon() : "no-cupon";
        Cupon cuponEncontrado = cuponPersistence.findCuponByCodigo(codigoCupon)
                .orElseThrow(()-> new CuponNotFoundException("cupon con cupon :"+codigoCupon+" no encontrado"));
        if (cuponEncontrado.getFechaExpiracion() != null && cuponEncontrado.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new CuponExpiredException("El cupón con código: " + codigoCupon + " ha expirado");
        }

        List<ItemCarrito> itemCarritoList = carritoRequest.itemcarritoList().stream()
                .map(itemCarritoRequest -> {
                    ProductoDto productoDto = productoFeingClient.getProductById(itemCarritoRequest.productoId());
                    if (productoDto==null){ throw new ProductNotFoundException(itemCarritoRequest.productoId());}
                    BigDecimal precioUnitarioItem = BigDecimal.valueOf(productoDto.getPrice() * itemCarritoRequest.cantidad())
                            .setScale(2, RoundingMode.HALF_UP);
                    return ItemCarrito.builder()
                            .productoId(productoDto.getId())
                            .cantidad(itemCarritoRequest.cantidad())
                            .precioUnitarioItem(precioUnitarioItem.floatValue())
                            .build();
                })
                .collect(Collectors.toList());

        BigDecimal totalCarrito = itemCarritoList.stream()
                .map(itemCarrito -> BigDecimal.valueOf(itemCarrito.getPrecioUnitarioItem()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

       if (cuponEncontrado.getTipoDescuento()== TipoDescuento.PORCENTAJE){
           BigDecimal descuento = BigDecimal.valueOf(cuponEncontrado.getCantidad() / 100.0);
            totalConDescuento = totalCarrito.multiply(BigDecimal.ONE.subtract(descuento))
                   .setScale(2, RoundingMode.HALF_UP);
       }
       else {
           BigDecimal descuento = BigDecimal.valueOf(cuponEncontrado.getCantidad()).setScale(2, RoundingMode.HALF_UP);
           totalConDescuento = totalCarrito.subtract(descuento).setScale(2, RoundingMode.HALF_UP);
       }


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
        UsuarioDto usuarioDto= usuarioFeingClient.getAllUsuarioById(carritoRequest.usuarioId());
        return new CarritoResponse(
                carritoGuardado.getCarritoId(),
                usuarioDto,
                carritoGuardado.getTotal(),
                carritoGuardado.getCupon().getCodigo(),
                itemCarritoResponseList
        );
    }

    @Override
    @Transactional
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
        UsuarioDto usuarioDto= usuarioFeingClient.getAllUsuarioById(carritoBd.getUsuarioId());

        return new CarritoResponse(
                carritoBd.getCarritoId(),
                usuarioDto,
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
    @Override
    public Page<Carrito> listCarritoPaginado(Long usuarioId, Pageable pageable) {
        if (usuarioId!=null){
                 usuarioFeingClient.getAllUsuarioById(usuarioId);
            return carritoPersistence.findAllByUsuarioId(usuarioId,pageable);
        }
        return carritoPersistence.findAll(pageable);
    }
}
