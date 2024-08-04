package com.msvccarritocompras.infrastructure.rest.controllers;

import com.msvccarritocompras.application.dto.CuponEditRequest;
import com.msvccarritocompras.application.dto.CuponRequest;
import com.msvccarritocompras.application.dto.ErrorResponse;
import com.msvccarritocompras.application.repository.CuponRepository;
import com.msvccarritocompras.domain.entity.Cupon;
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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "Cupones" , description = "Controllador para buscar,crear,editar y eliminar cupon de compra")
@RestController
@RequestMapping("/api/v1/cupones")
@RequiredArgsConstructor
public class CuponController {
    private final CuponRepository cuponRepository;

    @Operation(
            summary = "Lista de Cupones Paginado",
            description = "Lista de carrito , buscar por cupon , busca cupon activo",
            tags = {"Cupones"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de cupones encontrado exitosamente",
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
                            description = "Carrito con ese codigo no encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping
    ResponseEntity<?> getListCupon(@RequestParam(required = false) String codigo,
                                   @RequestParam(required = false) LocalDate fecha,
                                   @RequestParam(required = false,defaultValue = "false") String expirado,
                                   @RequestParam(required = false,defaultValue = "10") Integer size,
                                   @RequestParam(required = false,defaultValue = "0") Integer page
                                   ){
        Pageable pageable=PageRequest.of(page, size);

        Page<Cupon> cuponPage = cuponRepository.cuponList(codigo, fecha, expirado, pageable);
        if (cuponPage.isEmpty()) {
            return new ResponseEntity<>(cuponPage, HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(cuponPage);
    }
    @Operation(
            summary = "Buscar Cupon por su Id",
            description = "Buscar un cupon de compras por su Id",
            tags = {"Cupones"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Carrito encontrado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Cupon.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cupon con ese Id no encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("{cuponId}")
    ResponseEntity<?> getCuponId(@PathVariable Long cuponId){
        return ResponseEntity.ok(cuponRepository.cuponById(cuponId));
    }
    @Operation(
            summary = "Crear Cupon",
            description = "Crear un nuevo cupon de compras",
            tags = {"Cupones"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Cupon creado exitosamente"
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
    ResponseEntity<?> createCupon(@RequestBody @Validated CuponRequest cuponRequest){
        return new ResponseEntity<>(cuponRepository.createCupon(cuponRequest),HttpStatus.CREATED);
    }
    @Operation(
            summary = "Editar Cupon",
            description = "Editar un  cupon de compras",
            tags = {"Cupones"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cupon editado exitosamente"
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
    @PutMapping("/{cuponId}")
    ResponseEntity<?> editCupon(@PathVariable Long cuponId, @RequestBody @Validated CuponEditRequest cuponEditRequest){
        return ResponseEntity.ok(cuponRepository.editCupon(cuponId,cuponEditRequest));
    }

    @Operation(
            summary = "Elimar Cupon",
            description = "Elimar un  cupon de compras",
            tags = {"Cupones"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cupon eliminado exitosamente"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cupon no encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @DeleteMapping("{cuponId}")
    ResponseEntity<?> deleteCupon(@PathVariable Long cuponId){
        cuponRepository.deleteCupon(cuponId);
        Map<String,Object> resp= new HashMap<>();
        resp.put("mensaje","Cupon eliminado con el id "+cuponId);
        return ResponseEntity.ok(resp);
    }

}
