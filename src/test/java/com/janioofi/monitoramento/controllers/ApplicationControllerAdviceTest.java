package com.janioofi.monitoramento.controllers;

import com.janioofi.monitoramento.exceptions.ResourceNotFoundException;
import com.janioofi.monitoramento.exceptions.ValidationErrors;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationControllerAdviceTest {

    @InjectMocks
    private ApplicationControllerAdvice applicationControllerAdvice;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleNotFoundException_ShouldReturnNotFoundMessage() {
        String message = "Resource not found";
        ResourceNotFoundException ex = new ResourceNotFoundException(message);

        String result = applicationControllerAdvice.handleNotFoundException(ex);

        assertEquals(message, result);
    }

    @Test
    void validationErrors_ShouldReturnValidationErrors() {
        String path = "/test";

        FieldError fieldError = new FieldError("objectName", "fieldName", "defaultMessage");
        BindException bindException = mock(BindException.class);
        when(bindException.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(
                mock(org.springframework.core.MethodParameter.class), bindException);

        when(request.getRequestURI()).thenReturn(path);

        ValidationErrors result = applicationControllerAdvice.validationErrors(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        assertEquals("Validation Error", result.getError());
        assertEquals(path, result.getPath());
        assertEquals(1, result.getErrors().size());
        assertEquals("fieldName", result.getErrors().get(0).getFieldName());
        assertEquals("defaultMessage", result.getErrors().get(0).getMessage());
    }

    @Test
    void handleHttpMessageNotReadableException_ShouldReturnErrorMessage() {
        String message = "Message not readable";
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException(message);

        String result = applicationControllerAdvice.handleHttpMessageNotReadableException(ex);

        assertEquals(message, result);
    }

    @Test
    void dataIntegrityViolationException_ShouldReturnConflictMessage() {
        String message = "Data integrity violation";
        DataIntegrityViolationException ex = new DataIntegrityViolationException(message);

        String result = applicationControllerAdvice.dataIntegrityViolationException(ex);

        assertEquals(message, result);
    }
}
