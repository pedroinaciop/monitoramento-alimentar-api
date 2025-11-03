package com.monitoramento.saude.dto;


import java.sql.Date;

public record Email(
        String to,
        String subject,
        String body,
        Long usuarioId,
        Date dataInicial,
        Date dataFinal) {
}
