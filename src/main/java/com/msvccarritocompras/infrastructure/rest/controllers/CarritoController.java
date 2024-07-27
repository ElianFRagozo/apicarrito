package com.msvccarritocompras.infrastructure.rest.controllers;

import com.msvccarritocompras.application.dto.CarritoRequest;
import com.msvccarritocompras.application.dto.CarritoResponse;
import com.msvccarritocompras.application.repository.CarritoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoRepository carritoRepository;

    @PostMapping
    ResponseEntity<CarritoResponse> createCarrito(@RequestBody @Validated CarritoRequest carritoRequest){
        return new ResponseEntity<>(carritoRepository.crearCarrito(carritoRequest), HttpStatus.CREATED);
    }
}
