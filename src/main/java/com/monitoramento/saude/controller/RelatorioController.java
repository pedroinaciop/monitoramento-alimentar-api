package com.monitoramento.saude.controller;

import com.monitoramento.saude.service.RelatorioService;
import com.monitoramento.saude.dto.MedidasRelatorio;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {
    private final RelatorioService service;

    public RelatorioController(RelatorioService service) {
        this.service = service;
    }


    @PostMapping("/medidas")
    public String gerar(@RequestBody MedidasRelatorio param) throws IOException {
        service.relatorioMedidas(param.usuarioId(), param.dataInicial(), param.dataFinal());
        return "Relatório gerado com sucesso! Verifique a pasta C:\\jasper-report\\";
    }

    @PostMapping(value = "/medidas/download", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> download(@RequestBody MedidasRelatorio param) {
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
}