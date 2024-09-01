package com.janioofi.monitoramento.domain.dtos;

import com.janioofi.monitoramento.domain.enums.Level;

import java.util.UUID;

public record AlertRequestDto(
        UUID deviceId,
        String message,
        Level level
) {
}
