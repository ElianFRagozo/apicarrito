package com.msvccarritocompras.infrastructure.api.usuarios.client;


import com.msvccarritocompras.infrastructure.api.usuarios.dto.UsuarioDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "api-usuarios",url = "${url.client.api.usuarios}")
public interface UsuarioFeingClient {

    @GetMapping("/{usuarioId}")
    UsuarioDto getAllUsuarioById(@PathVariable Long usuarioId);
}
