package com.msvccarritocompras.application.repository;

import com.msvccarritocompras.application.dto.CuponEditRequest;
import com.msvccarritocompras.application.dto.CuponRequest;
import com.msvccarritocompras.domain.entity.Cupon;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CuponRepository {

    List<Cupon> cuponList(String codigo, LocalDate fecha,String expirado);
    Cupon cuponById(Long cuponId);
    Cupon createCupon(CuponRequest cuponRequest);
    Cupon cuponByCodigo(String codigo);
    Cupon editCupon(Long cuponId, CuponEditRequest cuponEditRequest );
    void deleteCupon(Long cuponId);
}
