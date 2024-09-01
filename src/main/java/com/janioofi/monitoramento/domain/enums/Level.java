package com.janioofi.monitoramento.domain.enums;

import lombok.Getter;

@Getter
public enum Level {
    NORMAL(0, "Normal"),
    ERRO(1, "Erro"),
    CRITICO(2, "Critico");

    private final Integer code;
    private final String description;

    Level(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
