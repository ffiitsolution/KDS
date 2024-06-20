package com.ffi.api.kds.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.ffi.api.kds.dto.ExceptionResponse;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandler {
    
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            // String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setCode(HttpStatus.BAD_REQUEST.value());
        exceptionResponse.setMessage(String.join(",", errors));
        exceptionResponse.setPath(request.getRequestURI());
        exceptionResponse.setTimestamp(new Date());
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(
            Exception ex,
            HttpServletRequest request
    ) {
        ex.printStackTrace();
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setPath(request.getRequestURI());
        exceptionResponse.setTimestamp(new Date());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }
}
