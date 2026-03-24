package com.idat.pe.Cus_Registro_Postulante.exception;

import com.idat.pe.Cus_Registro_Postulante.genericResponse.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GenericResponse<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String mensaje = "Error en los datos enviados";
        
        if (ex.getMessage().contains("Duplicate entry")) {
            mensaje = "Este documento o correo ya se encuentra registrado";
        } else if (ex.getMessage().contains("foreign key")) {
            mensaje = "Referencia de datos inválida";
        }
        
        GenericResponse<String, String> response = GenericResponse.<String, String>builder()
                .message(mensaje)
                .body(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<GenericResponse<String, String>> handleDataAccessException(DataAccessException ex) {
        GenericResponse<String, String> response = GenericResponse.<String, String>builder()
                .message("Error al acceder a la base de datos. Por favor, intente más tarde.")
                .body(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GenericResponse<String, String>> handleRuntimeException(RuntimeException ex) {
        GenericResponse<String, String> response = GenericResponse.<String, String>builder()
                .message("Error inesperado")
                .body(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse<String, Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));
        
        GenericResponse<String, Map<String, String>> response = GenericResponse.<String, Map<String, String>>builder()
                .message("Error de validación")
                .body(errors)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
