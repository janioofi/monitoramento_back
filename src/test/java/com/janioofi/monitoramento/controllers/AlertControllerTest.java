package com.janioofi.monitoramento.controllers;

import com.janioofi.monitoramento.domain.dtos.AlertRequestDto;
import com.janioofi.monitoramento.domain.dtos.AlertResponseDto;
import com.janioofi.monitoramento.domain.enums.Level;
import com.janioofi.monitoramento.domain.services.AlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertControllerTest {

    @Mock
    private AlertService alertService;

    @InjectMocks
    private AlertController alertController;

    private UUID alertId;
    private AlertRequestDto alertRequestDto;
    private AlertResponseDto alertResponseDto;

    @BeforeEach
    void setUp() {
        alertId = UUID.randomUUID();
        alertRequestDto = new AlertRequestDto("Critical alert", Level.CRITICO);
        alertResponseDto = new AlertResponseDto(alertId, "CRITICO", "Critical alert");
    }

    @Test
    void createAlert_ShouldCreateAndReturnAlert() {
        // Arrange
        when(alertService.createAlert(alertRequestDto)).thenReturn(alertResponseDto);

        // Act
        ResponseEntity<AlertResponseDto> response = alertController.createAlert(alertRequestDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(alertResponseDto, response.getBody());
        verify(alertService, times(1)).createAlert(alertRequestDto);
    }

    @Test
    void getAllAlerts_ShouldReturnListOfAlerts() {
        // Arrange
        List<AlertResponseDto> alertList = Arrays.asList(alertResponseDto);
        when(alertService.getAllAlerts()).thenReturn(alertList);

        // Act
        ResponseEntity<List<AlertResponseDto>> response = alertController.getAllAlerts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(alertList, response.getBody());
        verify(alertService, times(1)).getAllAlerts();
    }

    @Test
    void deleteAlert_ShouldDeleteAlert() {
        // Act
        ResponseEntity<Void> response = alertController.deleteAlert(alertId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(alertService, times(1)).deleteAlert(alertId);
    }
}
