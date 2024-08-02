package com.msvccarritocompras.infrastructure.persistence.specification;

import com.msvccarritocompras.domain.entity.Cupon;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class SpeceficationCupon implements Specification<Cupon> {

    private String codigo;
    private LocalDate fecha;
    private Boolean expirado;


    @Override
    public Predicate toPredicate(@NonNull Root<Cupon> root,
                                 @NonNull CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder criteriaBuilder) {

        query.orderBy(criteriaBuilder.asc(root.get("fechaExpiracion")));

        List<Predicate> predicateList=new ArrayList<>();

        if (StringUtils.hasText(codigo)){
            Expression<String> codigoToUpperCase=criteriaBuilder.upper(root.get("codigo"));
            Predicate codigoLikePredicate=criteriaBuilder.like(codigoToUpperCase,"%".concat(codigo).concat("%"));
            predicateList.add(codigoLikePredicate);
        }
        if (fecha!=null){
            Predicate fechaLimiteExpiracion=criteriaBuilder.lessThanOrEqualTo(root.get("fechaExpiracion"),fecha);
            predicateList.add(fechaLimiteExpiracion);
        }
        Predicate isCuponExpirado=criteriaBuilder.equal(root.get("isExpired"),expirado);
        predicateList.add(isCuponExpirado);


        return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
    }
}
