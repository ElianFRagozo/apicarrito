package com.msvccarritocompras.infrastructure.persistence.repository;

import com.msvccarritocompras.domain.entity.Carrito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoPersistence extends JpaRepository<Carrito,Long> {
    Page<Carrito> findAllByUsuarioId(Long usuarioId, Pageable pageable);
}
