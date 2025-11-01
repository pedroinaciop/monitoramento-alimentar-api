package com.monitoramento.saude.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoRefeicao {
    CAFE_MANHA("Café da Manhã"),
    LANCHE("Lanche"),
    ALMOCO("Almoço"),
    CAFE_TARDE("Café da Tarde"),
    JANTAR("Jantar"),
    CEIA("Ceia");

    private final String descricao;

    TipoRefeicao(String descricao) {
        this.descricao = descricao;
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
