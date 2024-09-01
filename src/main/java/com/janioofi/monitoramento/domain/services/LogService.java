package com.janioofi.monitoramento.domain.services;

import com.janioofi.monitoramento.domain.entities.Device;
import com.janioofi.monitoramento.domain.entities.Log;
import com.janioofi.monitoramento.domain.enums.Level;
import com.janioofi.monitoramento.domain.enums.Status;
import com.janioofi.monitoramento.domain.repositories.DeviceRepository;
import com.janioofi.monitoramento.domain.repositories.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final DeviceRepository deviceRepository;
    private final AlertService alertService;

    @Scheduled(fixedRate = 1_000 * 60)
    public void generateLogs() {
        List<Device> devices = deviceRepository.findAll();
        devices.forEach(device -> {
            checkAndUpdateDeviceStatus(device);
            logEvent(device, determineLevel(device.getStatus()), "Atualização");
        });
    }

    private void checkAndUpdateDeviceStatus(Device device) {
        if (isDeviceInactive(device)) {
            updateDeviceStatus(device, Status.INATIVO);
        } else {
            updateDeviceStatus(device, Status.EM_FALHA);
        }
        alertService.checkAndTriggerAlertsManually(device);
    }

    private boolean isDeviceInactive(Device device) {
        return device.getLastPing().isBefore(LocalDateTime.now().minusMinutes(2));
    }

    private void updateDeviceStatus(Device device, Status status) {
        device.setStatus(status);
        deviceRepository.save(device);
    }

    private Level determineLevel(Status status) {
        return switch (status) {
            case ATIVO -> Level.NORMAL;
            case EM_FALHA -> Level.ERRO;
            case INATIVO -> Level.CRITICO;
        };
    }

    public void logEvent(Device device, Level level, String message) {
        Log log = new Log();
        log.setDevice(device);
        log.setTimestamp(LocalDateTime.now());
        log.setMessage(message);
        log.setLevel(level);
        logRepository.save(log);
    }
}
