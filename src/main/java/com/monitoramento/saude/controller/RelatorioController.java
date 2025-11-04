package com.monitoramento.saude.controller;

import com.monitoramento.saude.dto.RefeicoesRelatorioDTO;
import com.monitoramento.saude.service.RelatorioService;
import com.monitoramento.saude.dto.MedidasRelatorioDTO;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RelatorioController.class);
    private final RelatorioService service;
    private static final String ARQUIVO_JASPER_MEDIDAS = "reports/medidas.jasper";
    private static final String ARQUIVO_JRXML_MEDIDAS = "reports/medidas.jrxml";

    public RelatorioController(RelatorioService service) {
        this.service = service;
    }

    @PostMapping(value = "/medidas/download", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Object> relatorioMedidasDownload(@RequestBody MedidasRelatorioDTO param) {
        LOGGER.info("📥 Recebida requisição para relatório de medidas: {}", param);

        try {
            byte[] pdf = service.relatorioMedidasDownload(param.usuarioId(), param.dataInicial(), param.dataFinal());

            LOGGER.info("✅ Relatório de medidas gerado com sucesso");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-medidas.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);

        } catch (Exception e) {
            LOGGER.error("❌ ERRO NO CONTROLLER - Relatório medidas", e);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro ao gerar relatório de medidas");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("type", e.getClass().getSimpleName());

            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);
        }
    }

    @PostMapping(value = "/refeicoes/download", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Object> relatorioRefeicoesDownload(@RequestBody RefeicoesRelatorioDTO param) {
        LOGGER.info("📥 Recebida requisição para relatório de refeições: {}", param);

        try {
            byte[] pdf = service.relatorioRefeicoesDownload(param.usuarioId(), param.dataInicial(), param.dataFinal());

            LOGGER.info("✅ Relatório de refeições gerado com sucesso");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-refeicoes.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);

        } catch (Exception e) {
            LOGGER.error("❌ ERRO NO CONTROLLER - Relatório refeições", e);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro ao gerar relatório de refeições");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("type", e.getClass().getSimpleName());

            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);
        }
    }

    @GetMapping("/debug/recursos")
    public ResponseEntity<Map<String, Object>> debugRecursos() {
        Map<String, Object> debugInfo = new HashMap<>();

        try {
            ClassPathResource jasperMedidas = new ClassPathResource(ARQUIVO_JASPER_MEDIDAS);
            ClassPathResource jrxmlMedidas = new ClassPathResource(ARQUIVO_JRXML_MEDIDAS);

            debugInfo.put("jasper_medidas_existe", jasperMedidas.exists());
            debugInfo.put("jrxml_medidas_existe", jrxmlMedidas.exists());
            debugInfo.put("jasper_medidas_path", jasperMedidas.getPath());

            return ResponseEntity.ok(debugInfo);

        } catch (Exception e) {
            debugInfo.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(debugInfo);
        }
    }
}