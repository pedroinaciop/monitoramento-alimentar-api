package com.monitoramento.saude.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monitoramento.saude.model.Usuario;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MedidasRequestDTO(
        Long id,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
        LocalDateTime dataRegistro,

        BigDecimal pesoAtual,

        BigDecimal pesoDesejado,

        BigDecimal medidaCintura,
        BigDecimal medidaQuadril,
        BigDecimal medidaTorax,

        BigDecimal medidaBracoDireito,
        BigDecimal medidaBracoEsquerdo,

        BigDecimal medidaCoxaDireita,
        BigDecimal medidaCoxaEsquerda,

        BigDecimal medidaPanturrilhaDireita,
        BigDecimal medidaPanturrilhaEsquerda,

        BigDecimal altura,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime dataAlteracao,

        Usuario usuario
) {
}
