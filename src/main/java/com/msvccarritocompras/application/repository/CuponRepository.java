package com.msvccarritocompras.application.repository;

import com.msvccarritocompras.application.dto.CuponEditRequest;
import com.msvccarritocompras.application.dto.CuponRequest;
import com.msvccarritocompras.domain.entity.Cupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CuponRepository {

    Page<Cupon> cuponList(String codigo, LocalDate fecha, String expirado, Pageable pageable);
    Cupon cuponById(Long cuponId);
    Cupon createCupon(CuponRequest cuponRequest);
    Cupon cuponByCodigo(String codigo);
    Cupon editCupon(Long cuponId, CuponEditRequest cuponEditRequest );
    void deleteCupon(Long cuponId);
}
