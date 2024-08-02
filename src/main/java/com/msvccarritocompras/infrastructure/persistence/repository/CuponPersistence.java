package com.msvccarritocompras.infrastructure.persistence.repository;

import com.msvccarritocompras.domain.entity.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuponPersistence extends JpaRepository<Cupon,Long> , JpaSpecificationExecutor<Cupon> {
    Optional<Cupon> findCuponByCodigo(String codigo);
}
