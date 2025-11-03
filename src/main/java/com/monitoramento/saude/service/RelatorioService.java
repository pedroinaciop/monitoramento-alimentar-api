package com.monitoramento.saude.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class RelatorioService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RelatorioService.class);

    private static final String ARQUIVO_JASPER_MEDIDAS = "reports/medidas.jasper";
    private static final String ARQUIVO_JASPER_REFEICOES = "reports/refeicoes.jasper";
    private static final String ARQUIVO_JRXML_MEDIDAS = "reports/medidas.jrxml";
    private static final String ARQUIVO_JRXML_REFEICOES = "reports/refeicoes.jrxml";

    @Autowired
    private DataSource dataSource;

    public byte[] relatorioMedidasDownload(Long usuarioId, Date dataInicial, Date dataFinal) {
        LOGGER.info("Iniciando relatório de medidas - usuarioId: {}, dataInicial: {}, dataFinal: {}",
                usuarioId, dataInicial, dataFinal);

        return gerarRelatorio(ARQUIVO_JASPER_MEDIDAS, ARQUIVO_JRXML_MEDIDAS, usuarioId, dataInicial, dataFinal);
    }

    public byte[] relatorioRefeicoesDownload(Long usuarioId, Date dataInicial, Date dataFinal) {
        LOGGER.info("Iniciando relatório de refeições - usuarioId: {}, dataInicial: {}, dataFinal: {}",
                usuarioId, dataInicial, dataFinal);

        return gerarRelatorio(ARQUIVO_JASPER_REFEICOES, ARQUIVO_JRXML_REFEICOES, usuarioId, dataInicial, dataFinal);
    }

    private byte[] gerarRelatorio(String caminhoJasper, String caminhoJrxml, Long usuarioId, Date dataInicial, Date dataFinal) {
        Connection connection = null;

        try {
            connection = testarConexao();

            Map<String, Object> params = prepararParametros(usuarioId, dataInicial, dataFinal);

            JasperReport report = carregarRelatorioCompilado(caminhoJasper, caminhoJrxml);
            LOGGER.info("Relatório carregado com sucesso: {}", caminhoJasper);

            JasperPrint print = JasperFillManager.fillReport(report, params, connection);
            LOGGER.info("Relatório preenchido com sucesso, exportando para PDF...");

            return JasperExportManager.exportReportToPdf(print);

        } catch (Exception e) {
            LOGGER.error("ERRO ao gerar relatório: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao gerar relatório: " + e.getMessage(), e);
        } finally {
            fecharConexao(connection);
        }
    }

    private Connection testarConexao() throws SQLException {
        Connection connection = dataSource.getConnection();

        if (connection.isValid(2)) {
            LOGGER.info("Conexão com banco de dados estabelecida com sucesso");
            return connection;
        } else {
            throw new SQLException("Conexão com banco de dados inválida");
        }
    }

    private Map<String, Object> prepararParametros(Long usuarioId, Date dataInicial, Date dataFinal) {
        Map<String, Object> params = new HashMap<>();
        params.put("P_USUARIO_ID", usuarioId);
        params.put("P_DATA_INICIAL", dataInicial);
        params.put("P_DATA_FINAL", dataFinal);

        LOGGER.debug("Parâmetros do relatório: usuarioId={}, dataInicial={}, dataFinal={}",
                usuarioId, dataInicial, dataFinal);

        return params;
    }

    private JasperReport carregarRelatorioCompilado(String caminhoJasper, String caminhoJrxml) {
        try {
            InputStream jasperStream = new ClassPathResource(caminhoJasper).getInputStream();
            if (jasperStream != null) {
                JasperReport report = (JasperReport) JRLoader.loadObject(jasperStream);
                jasperStream.close();
                LOGGER.info("Relatório compilado carregado: {}", caminhoJasper);
                return report;
            }
        } catch (Exception e) {
            LOGGER.warn("Não foi possível carregar .jasper, compilando do .jrxml: {}", e.getMessage());
        }
        return compilarDoJrxml(caminhoJrxml);
    }

    private JasperReport compilarDoJrxml(String caminhoJrxml) {
        try (InputStream jrxmlStream = new ClassPathResource(caminhoJrxml).getInputStream()) {
            JasperReport report = JasperCompileManager.compileReport(jrxmlStream);
            LOGGER.info("Relatório compilado com sucesso do .jrxml: {}", caminhoJrxml);
            return report;
        } catch (Exception e) {
            LOGGER.error("ERRO ao compilar relatório: {}", caminhoJrxml, e);
            throw new RuntimeException("Erro ao compilar relatório: " + caminhoJrxml, e);
        }
    }

    private void fecharConexao(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    LOGGER.debug("Conexão fechada com sucesso");
                }
            } catch (SQLException e) {
                LOGGER.warn("Aviso ao fechar conexão: {}", e.getMessage());
            }
        }
    }
}