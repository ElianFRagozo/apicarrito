package com.msvccarritocompras.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carritos")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carritoId;
    private Long usuarioId;
    private Float total;
    @OneToOne
    @JoinColumn(name = "cupon_id")
    private Cupon cupon;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "carrito_items",
            joinColumns = @JoinColumn(name = "carrito_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<ItemCarrito> itemCarritoList;
}
