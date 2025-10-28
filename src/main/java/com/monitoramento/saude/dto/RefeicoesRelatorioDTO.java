package com.monitoramento.saude.dto;

public record RefeicoesRelatorioDTO(
        Long usuarioId,
        java.sql.Date dataInicial,
        java.sql.Date dataFinal) {
}
