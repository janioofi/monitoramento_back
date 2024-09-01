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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final LogRepository logRepository;
    private final LogService logService;
    private static final String DEVICE_NOT_FOUND = "Dispositivo não encontrado com id ";

    public List<DeviceResponseDto> getAllDevices() {
        return deviceRepository.findAll().stream().map(Mapper::toDto).toList();
    }

    public DeviceResponseDto getDeviceById(UUID id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(DEVICE_NOT_FOUND + id));
        return Mapper.toDto(device);
    }

    public DeviceResponseDto createDevice(@Valid DeviceRequestDto request) {
        Device device = new Device();
        device.setName(request.name());
        device.setStatus(request.status());
        device.setLastPing(LocalDateTime.now());
        device.setLocation(request.location());
        log.info("Creating new device: {}", request.name());
        return Mapper.toDto(deviceRepository.save(device));
    }

    public DeviceResponseDto updateDevice(UUID id, @Valid DeviceRequestDto request) {
        Device device = deviceRepository.findById(id)
                .map(data -> {
                    data.setName(request.name());
                    data.setStatus(request.status());
                    data.setLastPing(LocalDateTime.now());
                    data.setLocation(request.location());
                    Device updatedDevice = deviceRepository.save(data);

                    logService.logEvent(updatedDevice, determineLevel(updatedDevice.getStatus()),  "Atualização no dispositivo");

                    return updatedDevice;
                })
                .orElseThrow(() -> new ResourceNotFoundException(DEVICE_NOT_FOUND + id));

        log.info("Performing an update to device with ID: {}", id);
        return Mapper.toDto(device);
    }

    private Level determineLevel(Status status) {
        return switch (status) {
            case EM_FALHA -> Level.ERRO;
            case INATIVO -> Level.CRITICO;
            default -> Level.NORMAL;
        };
    }

    public void delete(UUID id) {
        Optional<Device> payment = deviceRepository.findById(id);
        if (payment.isEmpty()) {
            throw new ResourceNotFoundException(DEVICE_NOT_FOUND + id);
        }
        log.info("Deleting device with ID: {}", id);
        deviceRepository.deleteById(id);
    }

    public List<LogResponseDto> getLogsByDeviceId(UUID deviceId) {
        List<Log> logs = logRepository.findByDeviceId(deviceId);
        return logs.stream().map(Mapper::toDto).toList();
    }
}
