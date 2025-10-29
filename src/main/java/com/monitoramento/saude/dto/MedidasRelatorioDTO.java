package com.monitoramento.saude.dto;

public record MedidasRelatorioDTO(
        Long usuarioId,
        java.sql.Date dataInicial,
        java.sql.Date dataFinal) {
}
