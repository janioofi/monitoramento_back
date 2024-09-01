package com.janioofi.monitoramento.controllers;

import com.janioofi.monitoramento.exceptions.ResourceNotFoundException;
import com.janioofi.monitoramento.exceptions.ValidationErrors;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(ResourceNotFoundException ex){
        log.error(ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrors validationErrors(MethodArgumentNotValidException ex, HttpServletRequest request){
        ValidationErrors field = new ValidationErrors();
        field.setPath(request.getRequestURI());
        field.setStatus(HttpStatus.BAD_REQUEST.value());
        field.setError("Validation Error");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            field.addErrors(error.getField(), error.getDefaultMessage());
        }
        log.error(field.toString());
        return field;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String dataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error(ex.getMessage());
        return ex.getMessage();
    }
}