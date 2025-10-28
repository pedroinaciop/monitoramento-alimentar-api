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
    public static final String ARQUIVOJRXMLMEDIDAS = "medidas.jrxml";
    public static final String ARQUIVOJRXMLREFEICOES = "refeicoes.jrxml";
    public static final Logger LOGGER = LoggerFactory.getLogger(RelatorioService.class);

    @Autowired
    private DataSource dataSource;

    public byte[] relatorioMedidasDownload(Long usuarioId, Date dataInicial, Date dataFinal) {
        LOGGER.info("usuarioId={}, dataInicial={}, dataFinal={}", usuarioId, dataInicial, dataFinal);
        Connection connection = null;

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("P_USUARIO_ID", usuarioId);
            params.put("P_DATA_INICIAL", dataInicial);
            params.put("P_DATA_FINAL", dataFinal);

            String pathAbsoluto = getAbsultePath(ARQUIVOJRXMLMEDIDAS);

            JasperReport report = JasperCompileManager.compileReport(pathAbsoluto);
            LOGGER.info("Relatório compilado");

            connection = dataSource.getConnection();
            JasperPrint print = JasperFillManager.fillReport(report, params, connection);

            return JasperExportManager.exportReportToPdf(print);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
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

            String pathAbsoluto = getAbsultePath(ARQUIVOJRXMLREFEICOES);

            JasperReport report = JasperCompileManager.compileReport(pathAbsoluto);
            LOGGER.info("Relatório compilado");

            connection = dataSource.getConnection();
            JasperPrint print = JasperFillManager.fillReport(report, params, connection);

            return JasperExportManager.exportReportToPdf(print);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                try { connection.close(); } catch (Exception e) { }
            }
        }
    }

    private String getAbsultePath(String file) {
        try {
            return ResourceUtils.getFile(file).getAbsolutePath();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Arquivo JRXML não encontrado: " + file, e);
        }
    }
}