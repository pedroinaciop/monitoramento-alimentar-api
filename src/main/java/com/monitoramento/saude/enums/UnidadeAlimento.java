package com.monitoramento.saude.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UnidadeAlimento {
    UNIDADE("Unidade"),
    MILIGRAMAS("Miligramas"),
    GRAMAS("Gramas"),
    QUILOS("Quilos"),
    MILILITROS("Mililitros"),
    LITROS("Litros"),
    COPO("Copo"),
    XICARA("Xícara"),
    TACA("Taça"),
    FATIA("Fatia"),
    PEDACO("Pedaço"),
    COLHER("Colher"),
    COLHER_CHA("Colher de Chá"),
    COLHER_SOPA("Colher de Sopa"),
    PITADA("Pitada");

    private final String simbolo;

    UnidadeAlimento(String simbolo) {
        this.simbolo = simbolo;
    }

    @JsonValue
    public String getSimbolo() {
        return simbolo;
    }
}
