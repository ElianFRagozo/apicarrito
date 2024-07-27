package com.msvccarritocompras.infrastructure.api.producto.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoDto {

    private Long id;
    private String title;
    private String description;
    private Float price;
    private String image;
}
