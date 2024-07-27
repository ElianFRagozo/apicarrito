package com.msvccarritocompras.domain.entity;


import com.msvccarritocompras.domain.enums.TipoDescuento;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "cupones")
public class Cupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cuponId;
    private String descripcion;
    @Column(unique = true)
    private String codigo;
    @Enumerated(EnumType.STRING)
    private TipoDescuento tipoDescuento;
    private Float cantidad;

}
