package com.msvccarritocompras.infrastructure.api.clientError;

import com.msvccarritocompras.infrastructure.api.producto.exceptions.ProductNotFoundException;
import com.msvccarritocompras.infrastructure.api.usuarios.exceptions.UsuarioNotFoundException;
import com.msvccarritocompras.infrastructure.api.interceptor.FeignClientInterceptor;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@Component
public class FeignClientErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

@Override
public Exception decode(String methodKey, Response response) {
    if (response.status() == HttpStatus.NOT_FOUND.value()) {
        if (methodKey.contains("UsuarioFeingClient")) {
            Long usuarioId = FeignClientInterceptor.getId();
            FeignClientInterceptor.clear();
            return new UsuarioNotFoundException(usuarioId);
        }
        if (methodKey.contains("ProductoFeingClient")) {
            Long productoId = FeignClientInterceptor.getId();
            FeignClientInterceptor.clear();
            return new ProductNotFoundException(productoId);
        }
    }

    return defaultErrorDecoder.decode(methodKey, response);
}
}