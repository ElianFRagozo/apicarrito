package com.msvccarritocompras.infrastructure.rest.controllers;

import com.msvccarritocompras.application.dto.CuponEditRequest;
import com.msvccarritocompras.application.dto.CuponRequest;
import com.msvccarritocompras.application.repository.CuponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cupones")
@RequiredArgsConstructor
public class CuponController {
    private final CuponRepository cuponRepository;

    @GetMapping
    ResponseEntity<?> getListCupon(@RequestParam(required = false) String codigo,
                                   @RequestParam(required = false) LocalDate fecha,
                                   @RequestParam(required = false,defaultValue = "false") String expirado){

        if (cuponRepository.cuponList(codigo,fecha,expirado).isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(cuponRepository.cuponList(codigo,fecha,expirado));
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
    ResponseEntity<?> editCupon(@PathVariable Long cuponId, @RequestBody @Validated CuponEditRequest cuponEditRequest){
        return ResponseEntity.ok(cuponRepository.editCupon(cuponId,cuponEditRequest));
    }

    @DeleteMapping("{cuponId}")
    ResponseEntity<?> deleteCupon(@PathVariable Long cuponId){
        cuponRepository.deleteCupon(cuponId);
        Map<String,Object> resp= new HashMap<>();
        resp.put("mensaje","Cupon eliminado con el id "+cuponId);
        return ResponseEntity.ok(resp);
    }

}
