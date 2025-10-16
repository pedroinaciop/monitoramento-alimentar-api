package com.monitoramento.saude.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NivelAtividadeFisica {
    SEDENTARIO("Sedentário"),
    LEVE("Leve"),
    MODERADO("Moderado"),
    INTENSO("Intenso");

    private final String descricao;

    NivelAtividadeFisica(String descricao) {
        this.descricao = descricao;
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }
}
