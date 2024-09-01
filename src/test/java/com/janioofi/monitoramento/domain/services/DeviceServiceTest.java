package com.janioofi.monitoramento.domain.services;

import com.janioofi.monitoramento.domain.dtos.DeviceRequestDto;
import com.janioofi.monitoramento.domain.dtos.DeviceResponseDto;
import com.janioofi.monitoramento.domain.dtos.LogResponseDto;
import com.janioofi.monitoramento.domain.dtos.Mapper;
import com.janioofi.monitoramento.domain.entities.Device;
import com.janioofi.monitoramento.domain.entities.Log;
import com.janioofi.monitoramento.domain.enums.Level;
import com.janioofi.monitoramento.domain.enums.Status;
import com.janioofi.monitoramento.domain.repositories.DeviceRepository;
import com.janioofi.monitoramento.domain.repositories.LogRepository;
import com.janioofi.monitoramento.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private LogRepository logRepository;

    @Mock
    private LogService logService;

    @InjectMocks
    private DeviceService deviceService;

    private UUID deviceId;
    private Device device;
    private DeviceRequestDto deviceRequestDto;
    private DeviceResponseDto deviceResponseDto;

    @BeforeEach
    void setUp() {
        deviceId = UUID.randomUUID();
        device = new Device();
        device.setIdDevice(deviceId);
        device.setName("Device1");
        device.setStatus(Status.ATIVO);
        device.setLastPing(LocalDateTime.now());
        device.setLocation("Location1");

        deviceRequestDto = new DeviceRequestDto("Device1", Status.ATIVO, "Location1");

        deviceResponseDto = Mapper.toDto(device);
    }

    @Test
    void getAllDevices_ShouldReturnListOfDevices() {
        // Arrange
        when(deviceRepository.findAll()).thenReturn(List.of(device));

        // Act
        List<DeviceResponseDto> result = deviceService.getAllDevices();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(deviceResponseDto, result.get(0));
        verify(deviceRepository, times(1)).findAll();
    }

    @Test
    void getDeviceById_ShouldReturnDeviceResponseDto_WhenDeviceExists() {
        // Arrange
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));

        // Act
        DeviceResponseDto result = deviceService.getDeviceById(deviceId);

        // Assert
        assertNotNull(result);
        assertEquals(deviceResponseDto, result);
        verify(deviceRepository, times(1)).findById(deviceId);
    }

    @Test
    void getDeviceById_ShouldThrowResourceNotFoundException_WhenDeviceDoesNotExist() {
        // Arrange
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> deviceService.getDeviceById(deviceId));
        assertEquals("Dispositivo não encontrado com id " + deviceId, exception.getMessage());
        verify(deviceRepository, times(1)).findById(deviceId);
    }

    @Test
    void createDevice_ShouldReturnCreatedDeviceResponseDto() {
        // Arrange
        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        // Act
        DeviceResponseDto result = deviceService.createDevice(deviceRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(deviceResponseDto, result);
        verify(deviceRepository, times(1)).save(any(Device.class));
    }

    @Test
    void updateDevice_ShouldReturnUpdatedDeviceResponseDto_WhenDeviceExists() {
        // Arrange
        Device updatedDevice = device;
        updatedDevice.setName("UpdatedDevice");
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));
        when(deviceRepository.save(any(Device.class))).thenReturn(updatedDevice);

        // Act
        DeviceResponseDto result = deviceService.updateDevice(deviceId, new DeviceRequestDto("UpdatedDevice", Status.ATIVO, "Location1"));

        // Assert
        assertNotNull(result);
        assertEquals(Mapper.toDto(updatedDevice), result);
        verify(deviceRepository, times(1)).findById(deviceId);
        verify(deviceRepository, times(1)).save(any(Device.class));
        verify(logService, times(1)).logEvent(updatedDevice, Level.NORMAL, "Atualização no dispositivo");
    }

    @Test
    void updateDevice_ShouldThrowResourceNotFoundException_WhenDeviceDoesNotExist() {
        // Arrange
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> deviceService.updateDevice(deviceId, deviceRequestDto));
        assertEquals("Dispositivo não encontrado com id " + deviceId, exception.getMessage());
        verify(deviceRepository, times(1)).findById(deviceId);
    }

    @Test
    void delete_ShouldDeleteDevice_WhenDeviceExists() {
        // Arrange
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));

        // Act
        deviceService.delete(deviceId);

        // Assert
        verify(deviceRepository, times(1)).findById(deviceId);
        verify(deviceRepository, times(1)).deleteById(deviceId);
    }

    @Test
    void delete_ShouldThrowResourceNotFoundException_WhenDeviceDoesNotExist() {
        // Arrange
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> deviceService.delete(deviceId));
        assertEquals("Dispositivo não encontrado com id " + deviceId, exception.getMessage());
        verify(deviceRepository, times(1)).findById(deviceId);
    }

    @Test
    void getLogsByDeviceId_ShouldReturnListOfLogResponseDto() {
        // Arrange
        Log log = new Log();
        log.setIdLog(UUID.randomUUID());
        log.setDevice(device);
        log.setTimestamp(LocalDateTime.now());
        log.setMessage("Log message");
        log.setLevel(Level.NORMAL);

        when(logRepository.findByDeviceId(deviceId)).thenReturn(List.of(log));

        // Act
        List<LogResponseDto> result = deviceService.getLogsByDeviceId(deviceId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Mapper.toDto(log), result.get(0));
        verify(logRepository, times(1)).findByDeviceId(deviceId);
    }
}
