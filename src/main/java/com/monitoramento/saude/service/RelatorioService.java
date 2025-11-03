package com.monitoramento.saude.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class RelatorioService {
    public static final String ARQUIVO_JASPER_MEDIDAS = "reports/medidas.jasper";
    public static final String ARQUIVO_JASPER_REFEICOES = "reports/refeicoes.jasper";
    public static final String ARQUIVO_JRXML_MEDIDAS = "reports/medidas.jrxml";
    public static final String ARQUIVO_JRXML_REFEICOES = "reports/refeicoes.jrxml";
    
    public static final Logger LOGGER = LoggerFactory.getLogger(RelatorioService.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ResourceLoader resourceLoader;

    public byte[] relatorioMedidasDownload(Long usuarioId, Date dataInicial, Date dataFinal) {
        LOGGER.info("usuarioId={}, dataInicial={}, dataFinal={}", usuarioId, dataInicial, dataFinal);
        Connection connection = null;

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("P_USUARIO_ID", usuarioId);
            params.put("P_DATA_INICIAL", dataInicial);
            params.put("P_DATA_FINAL", dataFinal);

            // Tenta carregar o relatório compilado (.jasper) primeiro
            JasperReport report = carregarRelatorioCompilado(ARQUIVO_JASPER_MEDIDAS, ARQUIVO_JRXML_MEDIDAS);
            LOGGER.info("Relatório de medidas carregado com sucesso");

            connection = dataSource.getConnection();
            JasperPrint print = JasperFillManager.fillReport(report, params, connection);

            return JasperExportManager.exportReportToPdf(print);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório de medidas: " + e.getMessage(), e);
        } finally {
            fecharConexao(connection);
        }
    }

    public byte[] relatorioRefeicoesDownload(Long usuarioId, Date dataInicial, Date dataFinal) {
        LOGGER.info("usuarioId={}, dataInicial={}, dataFinal={}", usuarioId, dataInicial, dataFinal);
        Connection connection = null;

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("P_USUARIO_ID", usuarioId);
            params.put("P_DATA_INICIAL", dataInicial);
            params.put("P_DATA_FINAL", dataFinal);

            // Tenta carregar o relatório compilado (.jasper) primeiro
            JasperReport report = carregarRelatorioCompilado(ARQUIVO_JASPER_REFEICOES, ARQUIVO_JRXML_REFEICOES);
            LOGGER.info("Relatório de refeições carregado com sucesso");

            connection = dataSource.getConnection();
            JasperPrint print = JasperFillManager.fillReport(report, params, connection);

            return JasperExportManager.exportReportToPdf(print);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório de refeições: " + e.getMessage(), e);
        } finally {
            fecharConexao(connection);
        }
    }

    /**
     * Tenta carregar o relatório compilado (.jasper) primeiro.
     * Se não encontrar, compila a partir do .jrxml
     */
    private JasperReport carregarRelatorioCompilado(String caminhoJasper, String caminhoJrxml) {
        try {
            // Primeiro tenta carregar o .jasper (pré-compilado)
            Resource resource = resourceLoader.getResource("classpath:" + caminhoJasper);
            if (resource.exists()) {
                try (InputStream inputStream = resource.getInputStream()) {
                    return (JasperReport) JRLoader.loadObject(inputStream);
                }
            } else {
                LOGGER.warn("Arquivo .jasper não encontrado, compilando a partir do .jrxml: {}", caminhoJasper);
                // Se não encontrar o .jasper, compila do .jrxml
                return compilarDoJrxml(caminhoJrxml);
            }
        } catch (Exception e) {
            LOGGER.warn("Erro ao carregar .jasper, tentando compilar do .jrxml: {}", e.getMessage());
            return compilarDoJrxml(caminhoJrxml);
        }
    }

    /**
     * Compila o relatório a partir do arquivo .jrxml
     */
    private JasperReport compilarDoJrxml(String caminhoJrxml) {
        try (InputStream inputStream = new ClassPathResource(caminhoJrxml).getInputStream()) {
            return JasperCompileManager.compileReport(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao compilar relatório do arquivo: " + caminhoJrxml, e);
        }
    }

    private void fecharConexao(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                LOGGER.warn("Erro ao fechar conexão: {}", e.getMessage());
            }
        }
    }
}
