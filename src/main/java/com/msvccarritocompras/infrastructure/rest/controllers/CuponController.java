package com.msvccarritocompras.infrastructure.rest.controllers;

import com.msvccarritocompras.application.dto.CuponEditRequest;
import com.msvccarritocompras.application.dto.CuponRequest;
import com.msvccarritocompras.application.repository.CuponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cupones")
@RequiredArgsConstructor
public class CuponController {
    private final CuponRepository cuponRepository;

    @GetMapping
    ResponseEntity<?> getListCupon(@RequestParam(required = false) String codigo){
        if (codigo!=null){
            return ResponseEntity.ok(cuponRepository.cuponByCodigo(codigo));
        }
        if (cuponRepository.cuponList().isEmpty()){
            return new ResponseEntity<>(cuponRepository.cuponList(), HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(cuponRepository.cuponList());
    }
    @GetMapping("{cuponId}")
    ResponseEntity<?> getCuponId(@PathVariable Long cuponId){
        return ResponseEntity.ok(cuponRepository.cuponById(cuponId));
    }
    @PostMapping
    ResponseEntity<?> createCupon(@RequestBody @Validated CuponRequest cuponRequest){
        return new ResponseEntity<>(cuponRepository.createCupon(cuponRequest),HttpStatus.CREATED);
    }
    @PutMapping("/{cuponId}")
    ResponseEntity<?> editCupo(@PathVariable Long cuponId, @RequestBody @Validated CuponEditRequest cuponEditRequest){
        return ResponseEntity.ok(cuponRepository.editCupon(cuponId,cuponEditRequest));
    }

}
