package com.msvccarritocompras.domain.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name="carrito_imagen_factura",
            joinColumns = @JoinColumn(name = "carrito_id"),
            inverseJoinColumns = @JoinColumn(name = "imagen_id"))
    private List<Imagen> imagenList;
}
