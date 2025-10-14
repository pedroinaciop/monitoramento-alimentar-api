package com.monitoramento.saude.dto;

import com.monitoramento.saude.enums.UnidadeAlimento;
import java.math.BigDecimal;

public record AlimentoRequestDTO(
        String nomeAlimento,
        UnidadeAlimento unidadeAlimento,
        BigDecimal quantidadeAlimento
) {}
