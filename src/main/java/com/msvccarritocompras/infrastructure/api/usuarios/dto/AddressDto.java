package com.msvccarritocompras.infrastructure.api.usuarios.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
    private String city;
    private String state;
    private String stateCode;
    private String postalCode;
}
