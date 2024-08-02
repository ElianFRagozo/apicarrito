package com.msvccarritocompras.application.service;

import com.msvccarritocompras.application.dto.CuponEditRequest;
import com.msvccarritocompras.application.dto.CuponRequest;
import com.msvccarritocompras.application.repository.CuponRepository;
import com.msvccarritocompras.domain.entity.Cupon;
import com.msvccarritocompras.domain.enums.TipoDescuento;
import com.msvccarritocompras.domain.exceptions.CuponCodigoExistenteException;
import com.msvccarritocompras.domain.exceptions.CuponNotFoundException;
import com.msvccarritocompras.domain.exceptions.InvalidCantidadException;
import com.msvccarritocompras.domain.exceptions.InvalidTipoDescuentoException;
import com.msvccarritocompras.infrastructure.persistence.repository.CuponPersistence;
import com.msvccarritocompras.infrastructure.persistence.specification.SpeceficationCupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;



@Service
@RequiredArgsConstructor
public class CuponService implements CuponRepository {

    private final CuponPersistence cuponPersistence;


    @Override
    public List<Cupon> cuponList(String codigo, LocalDate fecha, String expirado) {
        boolean isExpired=Boolean.parseBoolean(expirado);
        SpeceficationCupon speceficationCupon=new SpeceficationCupon(codigo,fecha,isExpired);

        return cuponPersistence.findAll(speceficationCupon);
    }

    @Override
    public Cupon cuponById(Long cuponId) {
        return cuponPersistence.findById(cuponId)
                .orElseThrow(()-> new CuponNotFoundException("cupon con el Id :"+cuponId+" no encontrado"));
    }

    @Override
    public Cupon createCupon(CuponRequest cuponRequest) {

        cuponPersistence.findCuponByCodigo(cuponRequest.codigo())
                .ifPresent(cuponExistente -> {
                    throw new CuponCodigoExistenteException("El código " + cuponRequest.codigo() + " ya pertenece a otro cupón");
                });

        TipoDescuento tipoDescuento;
        try {
            tipoDescuento = TipoDescuento.valueOf(cuponRequest.tipoDescuento().toUpperCase());
            if (tipoDescuento != TipoDescuento.MONTOFIJO && tipoDescuento != TipoDescuento.PORCENTAJE) {
                throw new InvalidTipoDescuentoException("El tipo de descuento debe ser MONTOFIJO o PORCENTAJE");
            }
        } catch (IllegalArgumentException e) {
            throw new InvalidTipoDescuentoException("El tipo de descuento debe ser MONTOFIJO o PORCENTAJE");
        }

        if (tipoDescuento == TipoDescuento.PORCENTAJE) {
            if (cuponRequest.cantidad() < 1 || cuponRequest.cantidad() > 100) {
                throw new InvalidCantidadException("La cantidad para un tipo de descuento PORCENTAJE debe ser mayor que 0 y menor o igual a 100");
            }
        } else {
            if (cuponRequest.cantidad() <= 0) {
                throw new InvalidCantidadException("La cantidad para un tipo de descuento MONTOFIJO debe ser mayor que 0");
            }
        }
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime fechaExpiracion=LocalDateTime.parse(cuponRequest.fechaExpiracion(),formatoFecha);
        boolean Expired= fechaExpiracion.isBefore(LocalDateTime.now());

        Cupon cupon = Cupon.builder()
                .tipoDescuento(tipoDescuento)
                .cantidad(cuponRequest.cantidad())
                .descripcion(cuponRequest.descripcion())
                .codigo(cuponRequest.codigo())
                .fechaExpiracion(fechaExpiracion)
                .isExpired(Expired)
                .build();

        return cuponPersistence.save(cupon);
    }

    @Override
    public Cupon cuponByCodigo(String codigo) {
        return cuponPersistence.findCuponByCodigo(codigo)
                .orElseThrow(()-> new CuponNotFoundException("cupon con cupon :"+codigo+" no encontrado"));
    }

    @Override
    public Cupon editCupon(Long cuponId, CuponEditRequest cuponEditRequest) {

        Cupon cuponBd = cuponPersistence.findById(cuponId)
                .orElseThrow(() -> new CuponNotFoundException("Cupón con el Id: " + cuponId + " no encontrado"));

        if (cuponEditRequest.descripcion() != null) {
            cuponBd.setDescripcion(cuponEditRequest.descripcion());
        }

        if (cuponEditRequest.codigo() != null) {
            cuponPersistence.findCuponByCodigo(cuponEditRequest.codigo())
                    .ifPresent(cuponExistente -> {
                        throw new CuponCodigoExistenteException("El código " + cuponEditRequest.codigo() + " ya pertenece a otro cupón");
                    });
            cuponBd.setCodigo(cuponEditRequest.codigo());
        }

        TipoDescuento tipoDescuento;
       if (cuponEditRequest.tipoDescuento()!=null){
           try {
               tipoDescuento = TipoDescuento.valueOf(cuponEditRequest.tipoDescuento().toUpperCase());
               if (tipoDescuento != TipoDescuento.MONTOFIJO && tipoDescuento != TipoDescuento.PORCENTAJE) {
                   throw new InvalidTipoDescuentoException("El tipo de descuento debe ser MONTOFIJO o PORCENTAJE");
               }
           } catch (IllegalArgumentException e) {
               throw new InvalidTipoDescuentoException("El tipo de descuento debe ser MONTOFIJO o PORCENTAJE");
           }
           cuponBd.setTipoDescuento(tipoDescuento);
       }

        if (cuponEditRequest.cantidad() != null) {
            tipoDescuento=cuponBd.getTipoDescuento();
            if (tipoDescuento == TipoDescuento.PORCENTAJE) {
                if (cuponEditRequest.cantidad() < 1 || cuponEditRequest.cantidad() > 100) {
                    throw new InvalidCantidadException("La cantidad para un tipo de descuento PORCENTAJE debe ser entre 1 y 100");
                }
            } else {
                if (cuponEditRequest.cantidad() <= 0) {
                    throw new InvalidCantidadException("La cantidad para un tipo de descuento MONTOFIJO debe ser mayor que 0");
                }
            }
            cuponBd.setCantidad(cuponEditRequest.cantidad());
        }

        if (cuponEditRequest.fechaExpiracion()!=null){
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime fechaExpiracion=LocalDateTime.parse(cuponEditRequest.fechaExpiracion(),formatoFecha);
            cuponBd.setFechaExpiracion(fechaExpiracion);

            boolean Expired= fechaExpiracion.isBefore(LocalDateTime.now());
            cuponBd.setIsExpired(Expired);
        }
        return cuponPersistence.save(cuponBd);
    }

    @Override
    public void deleteCupon(Long cuponId) {
        Cupon cuponBd = cuponPersistence.findById(cuponId)
                .orElseThrow(() -> new CuponNotFoundException("Cupón con el Id: " + cuponId + " no encontrado"));
        cuponPersistence.deleteById(cuponBd.getCuponId());
    }

}
