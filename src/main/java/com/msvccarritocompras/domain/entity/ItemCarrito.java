package com.msvccarritocompras.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items")
@Builder
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemCarritoId;
    private Long productoId;
    private Integer cantidad;
    private Float precioUnitarioItem;
}
