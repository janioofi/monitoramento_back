package com.janioofi.monitoramento.domain.dtos;


import java.time.LocalDateTime;
import java.util.UUID;

public record DeviceResponseDto(
        UUID idDevice,
        String name,
        String status,
        LocalDateTime lastPing,
        String location) {
}
