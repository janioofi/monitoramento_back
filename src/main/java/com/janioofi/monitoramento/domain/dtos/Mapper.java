package com.janioofi.monitoramento.domain.dtos;

import com.janioofi.monitoramento.domain.entities.Alert;
import com.janioofi.monitoramento.domain.entities.Device;
import com.janioofi.monitoramento.domain.entities.Log;
import com.janioofi.monitoramento.domain.entities.User;
import com.janioofi.monitoramento.exceptions.ResourceNotFoundException;

public class Mapper {

    private Mapper(){}

    public static DeviceResponseDto toDto(Device device){
        if (device == null) {
            throw new ResourceNotFoundException("Dispositivo não encontrado");
        }
        return new DeviceResponseDto(
                device.getIdDevice(),
                device.getName(),
                device.getStatus().getDescription(),
                device.getLastPing(),
                device.getLocation()
        );
    }

    public static UserResponseDto toDto(User user){
        if (user == null) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
        return new UserResponseDto(user.getIdUser(), user.getUsername());
    }

    public static LogResponseDto toDto(Log log){
        if (log == null) {
            throw new ResourceNotFoundException("Log não encontrado");
        }
        return new LogResponseDto(log.getIdLog(), log.getDevice().getName(),log.getTimestamp(), log.getMessage(),log.getLevel().getDescription());
    }

    public static AlertResponseDto toDto(Alert alert){
        if (alert == null) {
            throw new ResourceNotFoundException("Alerta não encontrado");
        }
        return new AlertResponseDto(alert.getIdAlert(), alert.getLevel().getDescription(), alert.getMessage(), alert.getDeviceId());
    }
}
