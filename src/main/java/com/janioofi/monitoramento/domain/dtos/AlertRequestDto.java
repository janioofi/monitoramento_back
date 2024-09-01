package com.janioofi.monitoramento.domain.dtos;

import com.janioofi.monitoramento.domain.enums.Level;

public record AlertRequestDto(
        String message,
        Level level
) {
}
