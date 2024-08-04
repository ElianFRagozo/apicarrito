package com.msvccarritocompras.infrastructure.api.producto.client;

import com.msvccarritocompras.infrastructure.api.clientError.FeignConfig;
import com.msvccarritocompras.infrastructure.api.producto.dto.ProductoDto;
import com.msvccarritocompras.infrastructure.api.producto.fallback.FallbackProductFeing;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@FeignClient(name = "api-producto", url = "${url.client.api.productos}",
        fallback = FallbackProductFeing.class,
        configuration = FeignConfig.class)
public interface ProductoFeingClient {
    @GetMapping("/{productoId}")
    ProductoDto getProductById(@PathVariable Long productoId);
}
