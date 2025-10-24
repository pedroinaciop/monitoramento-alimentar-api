package com.monitoramento.saude.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.sf.jasperreports.engine.*;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class RelatorioService {
    public static final String ARQUIVOJRXML = "medidas.jrxml";
    public static final Logger LOGGER = LoggerFactory.getLogger(RelatorioService.class);
    public static final String DESTINOPDF = "C:\\jasper-report\\";

    @Autowired
    private DataSource dataSource;

    public void relatorioMedidas(Long usuarioId, Date dataInicial, Date dataFinal) throws IOException {
        Connection connection = null;

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("P_USUARIO_ID", usuarioId);
            params.put("P_DATA_INICIAL", dataInicial);
            params.put("P_DATA_FINAL", dataFinal);

            String pathAbsoluto = getAbsultePath();
            String folderDiretorio = getDiretorioSave("medidas");

            // Compilar relatório
            JasperReport report = JasperCompileManager.compileReport(pathAbsoluto);
            LOGGER.info("Relatório compilado: {}", pathAbsoluto);

            // ✅ USAR CONEXÃO COM BANCO EM VEZ DE JREmptyDataSource
            connection = dataSource.getConnection();
            LOGGER.info("Conexão com banco obtida");

            JasperPrint print = JasperFillManager.fillReport(report, params, connection);
            LOGGER.info("Relatório preenchido com dados do banco");

            JasperExportManager.exportReportToPdfFile(print, folderDiretorio);
            LOGGER.info("PDF exportado para: {}", folderDiretorio);

        } catch (JRException e) {
            throw new RuntimeException("Erro JasperReports: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório: " + e.getMessage(), e);
        } finally {
            // Fechar conexão
            if (connection != null) {
                try {
                    connection.close();
                    LOGGER.info("Conexão fechada");
                } catch (Exception e) {
                    LOGGER.error("Erro ao fechar conexão", e);
                }
            }
        }
    }

    public byte[] relatorioMedidasDownload(Long usuarioId, Date dataInicial, Date dataFinal) {
        LOGGER.info("usuarioId={}, dataInicial={}, dataFinal={}", usuarioId, dataInicial, dataFinal);
        Connection connection = null;

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("P_USUARIO_ID", usuarioId);
            params.put("P_DATA_INICIAL", dataInicial);
            params.put("P_DATA_FINAL", dataFinal);

            String pathAbsoluto = getAbsultePath();

            JasperReport report = JasperCompileManager.compileReport(pathAbsoluto);
            LOGGER.info("Relatório compilado");

            // Usar conexão com banco
            connection = dataSource.getConnection();
            JasperPrint print = JasperFillManager.fillReport(report, params, connection);

            // Retornar bytes para download
            return JasperExportManager.exportReportToPdf(print);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                try { connection.close(); } catch (Exception e) { }
            }
        }
    }

    private String getDiretorioSave(String name) {
        this.createDiretorio(DESTINOPDF);
        return DESTINOPDF + name.concat(".pdf");
    }

    private void createDiretorio(String name) {
        File dir = new File(name);
        if(!dir.exists()){
            dir.mkdir();
        }
    }

    private String getAbsultePath() {
        try {
            return ResourceUtils.getFile(ARQUIVOJRXML).getAbsolutePath();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Arquivo JRXML não encontrado: " + ARQUIVOJRXML, e);
        }
    }
}