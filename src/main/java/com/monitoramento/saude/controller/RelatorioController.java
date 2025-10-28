package com.monitoramento.saude.controller;

import com.monitoramento.saude.dto.RefeicoesRelatorioDTO;
import com.monitoramento.saude.service.RelatorioService;
import com.monitoramento.saude.dto.MedidasRelatorioDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {
    private final RelatorioService service;

    public RelatorioController(RelatorioService service) {
        this.service = service;
    }

    @PostMapping(value = "/medidas/download", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> relatorioMedidasDownload(@RequestBody MedidasRelatorioDTO param) {
        try {
            byte[] pdf = service.relatorioMedidasDownload(param.usuarioId(), param.dataInicial(), param.dataFinal());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-medidas.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(("Erro: " + e.getMessage()).getBytes());
        }
    }

    @PostMapping(value = "/refeicoes/download", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> relatorioRefeicoesDownload(@RequestBody RefeicoesRelatorioDTO param) {
        try {
            byte[] pdf = service.relatorioRefeicoesDownload(param.usuarioId(), param.dataInicial(), param.dataFinal());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-refeicoes.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(("Erro: " + e.getMessage()).getBytes());
        }
    }

}