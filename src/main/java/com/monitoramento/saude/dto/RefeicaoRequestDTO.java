package com.monitoramento.saude.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.monitoramento.saude.enums.TipoRefeicao;
import com.monitoramento.saude.model.Usuario;
import java.time.LocalDateTime;
import java.util.List;

public record RefeicaoRequestDTO (
        Long id,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
        LocalDateTime dataRegistro,

        TipoRefeicao tipoRefeicao,

        Usuario usuario,

        List<AlimentoRequestDTO> alimentos,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime dataAlteracao) {
}
