package com.monitoramento.saude.dto;

import com.monitoramento.saude.enums.UnidadeAlimento;
import java.math.BigDecimal;

public record AlimentoResponseDTO(
        Long id,
        String nomeAlimento,
        UnidadeAlimento unidadeAlimento,
        BigDecimal quantidadeAlimento
) {}
