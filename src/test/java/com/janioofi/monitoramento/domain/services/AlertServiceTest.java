package com.janioofi.monitoramento.domain.services;

import com.janioofi.monitoramento.domain.dtos.AlertRequestDto;
import com.janioofi.monitoramento.domain.dtos.AlertResponseDto;
import com.janioofi.monitoramento.domain.dtos.Mapper;
import com.janioofi.monitoramento.domain.entities.Alert;
import com.janioofi.monitoramento.domain.entities.Device;
import com.janioofi.monitoramento.domain.entities.EmailModel;
import com.janioofi.monitoramento.domain.entities.Log;
import com.janioofi.monitoramento.domain.enums.Level;
import com.janioofi.monitoramento.domain.enums.Status;
import com.janioofi.monitoramento.domain.repositories.AlertRepository;
import com.janioofi.monitoramento.domain.repositories.DeviceRepository;
import com.janioofi.monitoramento.domain.repositories.LogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private LogRepository logRepository;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AlertService alertService;

    private UUID alertId;
    private Alert alert;
    private AlertRequestDto alertRequestDto;
    private Device device;
    private Log log;

    @BeforeEach
    void setUp() {
        alertId = UUID.randomUUID();
        alert = new Alert();
        alert.setIdAlert(alertId);
        alert.setLevel(Level.ERRO);
        alert.setMessage("Error Alert");

        alertRequestDto = new AlertRequestDto("Error Alert", Level.ERRO);

        device = new Device();
        device.setStatus(Status.EM_FALHA);
        device.setLastPing(LocalDateTime.now());

        log = new Log();
        log.setIdLog(UUID.randomUUID());
        log.setDevice(device);
        log.setLevel(Level.ERRO);
        log.setTimestamp(LocalDateTime.now());
        log.setMessage("Log Message");
    }

    @Test
    void createAlert_ShouldReturnCreatedAlertResponseDto() {
        // Arrange
        alert.setIdAlert(UUID.randomUUID()); // Simulando a geração do ID pelo banco de dados
        when(alertRepository.save(any(Alert.class))).thenAnswer(invocation -> {
            Alert savedAlert = invocation.getArgument(0);
            savedAlert.setIdAlert(alert.getIdAlert()); // Definindo o ID no alerta salvo
            return savedAlert;
        });

        // Act
        AlertResponseDto result = alertService.createAlert(alertRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(Mapper.toDto(alert), result);
        verify(alertRepository, times(1)).save(any(Alert.class));
    }

    @Test
    void getAllAlerts_ShouldReturnListOfAlertResponseDto() {
        // Arrange
        when(alertRepository.findAll()).thenReturn(List.of(alert));

        // Act
        List<AlertResponseDto> result = alertService.getAllAlerts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Mapper.toDto(alert), result.get(0));
        verify(alertRepository, times(1)).findAll();
    }

    @Test
    void deleteAlert_ShouldDeleteAlert() {
        // Act
        alertService.deleteAlert(alertId);

        // Assert
        verify(alertRepository, times(1)).deleteById(alertId);
    }

    @Test
    void checkAndTriggerAlertsManually_ShouldTriggerAlert_WhenDeviceLevelMatchesAlertLevel() {
        // Arrange
        when(alertRepository.findAll()).thenReturn(List.of(alert));

        // Act
        alertService.checkAndTriggerAlertsManually(device);

        // Assert
        verify(logRepository, times(1)).save(any(Log.class));
        verify(emailService, times(1)).sendEmail(any(EmailModel.class));
    }

    @Test
    void checkAndTriggerAlertsManually_ShouldNotTriggerAlert_WhenDeviceLevelDoesNotMatchAlertLevel() {
        // Arrange
        device.setStatus(Status.ATIVO); // Status that does not match the alert level
        when(alertRepository.findAll()).thenReturn(List.of(alert));

        // Act
        alertService.checkAndTriggerAlertsManually(device);

        // Assert
        verify(logRepository, never()).save(any(Log.class));
        verify(emailService, never()).sendEmail(any(EmailModel.class));
    }
}
