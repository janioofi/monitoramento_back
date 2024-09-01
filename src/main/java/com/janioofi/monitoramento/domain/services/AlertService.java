package com.janioofi.monitoramento.domain.services;

import com.janioofi.monitoramento.domain.dtos.AlertRequestDto;
import com.janioofi.monitoramento.domain.dtos.AlertResponseDto;
import com.janioofi.monitoramento.domain.dtos.Mapper;
import com.janioofi.monitoramento.domain.entities.Alert;
import com.janioofi.monitoramento.domain.entities.Device;
import com.janioofi.monitoramento.domain.entities.Log;
import com.janioofi.monitoramento.domain.enums.Level;
import com.janioofi.monitoramento.domain.enums.Status;
import com.janioofi.monitoramento.domain.repositories.AlertRepository;
import com.janioofi.monitoramento.domain.repositories.DeviceRepository;
import com.janioofi.monitoramento.domain.repositories.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

    private final AlertRepository alertRepository;
    private final LogRepository logRepository;
    private final DeviceRepository deviceRepository;

    @Scheduled(fixedRate = 1_000 * 60)
    public void checkLogsForAlerts() {
        List<Device> devices = deviceRepository.findAll();
        List<Alert> alerts = alertRepository.findAll();

        devices.forEach(device -> {
            Level deviceLevel = mapStatusToLevel(device.getStatus());
            alerts.forEach(alert -> {
                if (deviceLevel.equals(alert.getLevel())) {
                    Log log = createLog(device, alert.getMessage(), deviceLevel);
                    logRepository.save(log);
                    triggerAlert(alert, log);
                }
            });
        });
    }

    public void checkAndTriggerAlertsManually(Device device) {
        List<Alert> alerts = alertRepository.findAll();
        alerts.forEach(alert -> checkAndTriggerAlert(device, alert));
    }

    private void checkAndTriggerAlert(Device device, Alert alert) {
        Level deviceLevel = mapStatusToLevel(device.getStatus());
        if (deviceLevel.equals(alert.getLevel())) {
            Log log = createLog(device, alert.getMessage(), deviceLevel);
            logRepository.save(log);
            triggerAlert(alert, log);
        }
    }

    public AlertResponseDto createAlert(AlertRequestDto requestDto) {
        Alert alert = new Alert();
        alert.setLevel(requestDto.level());
        alert.setDeviceId(requestDto.deviceId());
        alert.setMessage(requestDto.message());
        alertRepository.save(alert);
        return Mapper.toDto(alert);
    }

    public List<AlertResponseDto> getAllAlerts() {
        return alertRepository.findAll().stream()
                .map(Mapper::toDto)
                .toList();
    }

    public void deleteAlert(UUID id) {
        alertRepository.deleteById(id);
    }

    private void triggerAlert(Alert alert, Log data) {
        log.error("Triggering alert for device {}: {}", data.getDevice().getIdDevice(), alert.getMessage());
    }

    private Log createLog(Device device, String message, Level level) {
        Log log = new Log();
        log.setDevice(device);
        log.setLevel(level);
        log.setTimestamp(LocalDateTime.now());
        log.setMessage(message);
        return log;
    }

    private Level mapStatusToLevel(Status status) {
        return switch (status) {
            case EM_FALHA -> Level.ERRO;
            case INATIVO -> Level.CRITICO;
            default -> Level.NORMAL;
        };
    }
}
