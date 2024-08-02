package com.msvccarritocompras.infrastructure.api.usuarios.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private String email;
    private String phone;
    private String username;
    private AddressDto address;
}
