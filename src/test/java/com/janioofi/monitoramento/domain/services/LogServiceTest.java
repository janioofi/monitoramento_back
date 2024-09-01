package com.janioofi.monitoramento.domain.services;

import com.janioofi.monitoramento.domain.entities.Device;
import com.janioofi.monitoramento.domain.entities.Log;
import com.janioofi.monitoramento.domain.enums.Level;
import com.janioofi.monitoramento.domain.enums.Status;
import com.janioofi.monitoramento.domain.repositories.DeviceRepository;
import com.janioofi.monitoramento.domain.repositories.LogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    @Mock
    private LogRepository logRepository;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private AlertService alertService;

    @InjectMocks
    private LogService logService;

    @Captor
    private ArgumentCaptor<Log> logCaptor;

    private Device device;

    @BeforeEach
    void setUp() {
        device = new Device();
        device.setIdDevice(UUID.randomUUID());
        device.setName("Test Device");
        device.setLocation("Test Location");
        device.setLastPing(LocalDateTime.now().minusSeconds(30));
    }

    @Test
    void generateLogs_WhenDeviceIsActive_ShouldLogNormalLevel() {
        device.setLastPing(LocalDateTime.now().minusSeconds(30)); // Menos de 1 minuto
        when(deviceRepository.findAll()).thenReturn(List.of(device));

        logService.generateLogs();

        verify(logRepository, times(1)).save(logCaptor.capture());
        Log savedLog = logCaptor.getValue();

        assertEquals(Level.NORMAL, savedLog.getLevel());
        assertEquals(Status.ATIVO, device.getStatus());
        verify(alertService, times(1)).checkAndTriggerAlertsManually(device);
    }

    @Test
    void generateLogs_WhenDeviceIsInFailure_ShouldLogErrorLevel() {
        device.setLastPing(LocalDateTime.now().minusMinutes(1).minusSeconds(30)); // Entre 1 e 2 minutos
        when(deviceRepository.findAll()).thenReturn(List.of(device));

        logService.generateLogs();

        verify(logRepository, times(1)).save(logCaptor.capture());
        Log savedLog = logCaptor.getValue();

        assertEquals(Level.ERRO, savedLog.getLevel());
        assertEquals(Status.EM_FALHA, device.getStatus());
        verify(alertService, times(1)).checkAndTriggerAlertsManually(device);
    }

    @Test
    void generateLogs_WhenDeviceIsInactive_ShouldLogCriticalLevel() {
        device.setLastPing(LocalDateTime.now().minusMinutes(3)); // Mais de 2 minutos
        when(deviceRepository.findAll()).thenReturn(List.of(device));

        logService.generateLogs();

        verify(logRepository, times(1)).save(logCaptor.capture());
        Log savedLog = logCaptor.getValue();

        assertEquals(Level.CRITICO, savedLog.getLevel());
        assertEquals(Status.INATIVO, device.getStatus());
        verify(alertService, times(1)).checkAndTriggerAlertsManually(device);
    }

    @Test
    void generateLogs_WhenNoDevices_ShouldNotLogAnything() {
        when(deviceRepository.findAll()).thenReturn(Collections.emptyList());

        logService.generateLogs();

        verify(logRepository, times(0)).save(any(Log.class));
        verify(alertService, times(0)).checkAndTriggerAlertsManually(any(Device.class));
    }
}
