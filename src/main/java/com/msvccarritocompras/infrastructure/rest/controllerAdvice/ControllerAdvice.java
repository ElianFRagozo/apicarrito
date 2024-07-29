package com.msvccarritocompras.infrastructure.rest.controllerAdvice;

import com.msvccarritocompras.domain.exceptions.CarritoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<?> errorConxion(ConnectException ex){
        Map<String,Object> error=new HashMap<>();
        error.put("error", HttpStatus.INTERNAL_SERVER_ERROR);
        error.put("codigo",HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("mensaje",ex.getMessage());

        return  new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> method(MethodArgumentNotValidException ex){

        List<FieldError> fieldErrors=ex.getBindingResult().getFieldErrors();
        Map<String,Object> errores=new HashMap<>();
        errores.put("status",HttpStatus.BAD_REQUEST);
        for (FieldError error : fieldErrors) {
            errores.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errores,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CarritoNotFoundException.class)
    public ResponseEntity<?> carritoNoEncontrado(CarritoNotFoundException ex){
        Map<String,Object> error=new HashMap<>();
        error.put("error", HttpStatus.NOT_FOUND);
        error.put("codigo",HttpStatus.NOT_FOUND.value());
        error.put("mensaje",ex.getMessage());

        return  new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

}