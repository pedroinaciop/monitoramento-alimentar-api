package com.monitoramento.saude.dto;

import java.sql.Date;

public record MedidasRelatorio(
        Long usuarioId,
        java.sql.Date dataInicial,
        java.sql.Date dataFinal) {
}
