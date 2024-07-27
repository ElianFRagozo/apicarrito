package com.msvccarritocompras.infrastructure.persistence;

import com.msvccarritocompras.domain.entity.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuponPersistence extends JpaRepository<Cupon,Long> {
    Cupon findCuponByCodigo(String codigo);
}
