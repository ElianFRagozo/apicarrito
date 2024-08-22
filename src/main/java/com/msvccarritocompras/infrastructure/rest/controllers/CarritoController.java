package com.msvccarritocompras.infrastructure.rest.controllers;

import com.msvccarritocompras.application.dto.CarritoRequest;
import com.msvccarritocompras.application.dto.CarritoResponse;
import com.msvccarritocompras.application.dto.ErrorResponse;
import com.msvccarritocompras.application.repository.CarritoRepository;
import com.msvccarritocompras.domain.entity.Carrito;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Carrito" , description = "Controllador para crear carrito y consultar el carrito")
@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoRepository carritoRepository;

    @Operation(
            summary = "Lista de Carrito Paginado",
            description = "Lista de carrito , buscar por usuarioId ",
            tags = {"Carrito"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de carrito encontrado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "Sin contenido",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuario con  ese iD no encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping
    ResponseEntity<?> listaCarritoPaginado(@RequestParam(required = false) Long usuarioId,
                                           @RequestParam(required = false,defaultValue = "10") Integer size,
                                           @RequestParam(required = false,defaultValue = "0") Integer page){

        Pageable pageable= PageRequest.of(page, size);
        Page<Carrito> carritoPage = carritoRepository.listCarritoPaginado(usuarioId,pageable);

        if (carritoPage.isEmpty()) {
            return new ResponseEntity<>(carritoPage, HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(carritoPage);
    }
    @Operation(
            summary = "Crear Carrito",
            description = "Crear un nuevo carrito de compras",
            tags = {"Carrito"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Carrito creado exitosamente"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Peticion no valida",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PostMapping
    ResponseEntity<CarritoResponse> createCarrito(@RequestBody @Validated CarritoRequest carritoRequest){
        return new ResponseEntity<>(carritoRepository.crearCarrito(carritoRequest), HttpStatus.CREATED);
    }
    @Operation(
            summary = "Buscar Carrito por su Id",
            description = "Buscar un carrito de compras por su Id",
            tags = {"Carrito"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Carrito encontrado exitosamente"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Carrito con ese Id no encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/{carritoId}")
    ResponseEntity<CarritoResponse> getCarritoById(@PathVariable Long carritoId){
        return ResponseEntity.ok(carritoRepository.getCarrito(carritoId));
    }

    @Operation(
            summary = "Buscar url factura del carrito por su Id",
            description = "Buscar las urls de la factura del carrito de compras por su Id",
            tags = {"Carrito"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Carrito encontrado exitosamente"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Carrito con ese Id no encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/factura/{carritoId}")
    ResponseEntity<?> getFacturaByCarritoId(@PathVariable Long carritoId){
        return ResponseEntity.ok(carritoRepository.getUrlFactura(carritoId));
    }
}
