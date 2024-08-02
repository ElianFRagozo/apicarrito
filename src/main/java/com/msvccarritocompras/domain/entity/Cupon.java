package com.msvccarritocompras.domain.entity;


import com.msvccarritocompras.domain.enums.TipoDescuento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaExpiracion;
    private Boolean isExpired;

    @PrePersist
    void utils(){
        fechaCreacion=LocalDateTime.now();
    }
}
