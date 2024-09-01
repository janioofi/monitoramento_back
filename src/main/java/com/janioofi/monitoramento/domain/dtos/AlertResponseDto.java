package com.janioofi.monitoramento.domain.dtos;

import com.janioofi.monitoramento.domain.entities.Device;

import java.util.UUID;

public record AlertResponseDto(
        UUID id,
        String level,
        String message
) {
}
