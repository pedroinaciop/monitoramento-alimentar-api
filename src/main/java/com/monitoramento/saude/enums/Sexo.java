package com.monitoramento.saude.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Sexo {
    MASCULINO("Masculino"),
    FEMININO("Feminino"),
    NAO_ESPECIFICAR("Não especificar");

    private final String descricao;

    Sexo(String descricao) {
        this.descricao = descricao;
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }
}
