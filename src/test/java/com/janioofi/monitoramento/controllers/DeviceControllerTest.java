package com.janioofi.monitoramento.controllers;

import com.janioofi.monitoramento.domain.dtos.DeviceRequestDto;
import com.janioofi.monitoramento.domain.dtos.DeviceResponseDto;
import com.janioofi.monitoramento.domain.dtos.LogResponseDto;
import com.janioofi.monitoramento.domain.enums.Status;
import com.janioofi.monitoramento.domain.services.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceControllerTest {

    @Mock
    private DeviceService deviceService;

    @InjectMocks
    private DeviceController deviceController;

    private UUID deviceId;
    private DeviceRequestDto deviceRequestDto;
    private DeviceResponseDto deviceResponseDto;

    @BeforeEach
    void setUp() {
        deviceId = UUID.randomUUID();
        deviceRequestDto = new DeviceRequestDto("Device1", Status.ATIVO, "Location1");
        deviceResponseDto = new DeviceResponseDto(deviceId, "Device1", "ATIVO", LocalDateTime.now(), "Location1");
    }

    @Test
    void getAllDevices_ShouldReturnListOfDevices() {
        // Arrange
        List<DeviceResponseDto> deviceList = Arrays.asList(deviceResponseDto);
        when(deviceService.getAllDevices()).thenReturn(deviceList);

        // Act
        ResponseEntity<List<DeviceResponseDto>> response = deviceController.getAllDevices();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deviceList, response.getBody());
        verify(deviceService, times(1)).getAllDevices();
    }

    @Test
    void getDeviceById_ShouldReturnDevice() {
        // Arrange
        when(deviceService.getDeviceById(deviceId)).thenReturn(deviceResponseDto);

        // Act
        ResponseEntity<DeviceResponseDto> response = deviceController.getDeviceById(deviceId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deviceResponseDto, response.getBody());
        verify(deviceService, times(1)).getDeviceById(deviceId);
    }

    @Test
    void createDevice_ShouldCreateAndReturnDevice() {
        // Arrange
        when(deviceService.createDevice(deviceRequestDto)).thenReturn(deviceResponseDto);

        // Act
        ResponseEntity<DeviceResponseDto> response = deviceController.createDevice(deviceRequestDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(deviceResponseDto, response.getBody());
        verify(deviceService, times(1)).createDevice(deviceRequestDto);
    }

    @Test
    void updateDevice_ShouldUpdateAndReturnDevice() {
        // Arrange
        when(deviceService.updateDevice(deviceId, deviceRequestDto)).thenReturn(deviceResponseDto);

        // Act
        ResponseEntity<DeviceResponseDto> response = deviceController.updateDevice(deviceId, deviceRequestDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deviceResponseDto, response.getBody());
        verify(deviceService, times(1)).updateDevice(deviceId, deviceRequestDto);
    }

    @Test
    void deleteDevice_ShouldDeleteDevice() {
        // Act
        ResponseEntity<Void> response = deviceController.deleteDevice(deviceId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deviceService, times(1)).delete(deviceId);
    }

    @Test
    void getLogsByDeviceId_ShouldReturnLogs() {
        // Arrange
        List<LogResponseDto> logs = Arrays.asList(
                new LogResponseDto(UUID.randomUUID(), "Device1", LocalDateTime.now(), "Log message 1", "ERROR"),
                new LogResponseDto(UUID.randomUUID(), "Device1", LocalDateTime.now(), "Log message 2", "INFO")
        );
        when(deviceService.getLogsByDeviceId(deviceId)).thenReturn(logs);

        // Act
        ResponseEntity<List<LogResponseDto>> response = deviceController.getLogsByDeviceId(deviceId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(logs, response.getBody());
        verify(deviceService, times(1)).getLogsByDeviceId(deviceId);
    }
}
