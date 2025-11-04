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
        LOGGER.info("=== INICIANDO RELATÓRIO MEDIDAS ===");
        LOGGER.info("UsuarioId: {}, DataInicial: {}, DataFinal: {}", usuarioId, dataInicial, dataFinal);

        try {
            return gerarRelatorio(ARQUIVO_JASPER_MEDIDAS, ARQUIVO_JRXML_MEDIDAS, usuarioId, dataInicial, dataFinal);
        } catch (Exception e) {
            LOGGER.error("*** ERRO CRÍTICO NO RELATÓRIO MEDIDAS ***", e);
            throw e;
        }
    }

    public byte[] relatorioRefeicoesDownload(Long usuarioId, Date dataInicial, Date dataFinal) {
        LOGGER.info("=== INICIANDO RELATÓRIO REFEIÇÕES ===");
        LOGGER.info("UsuarioId: {}, DataInicial: {}, DataFinal: {}", usuarioId, dataInicial, dataFinal);

        try {
            return gerarRelatorio(ARQUIVO_JASPER_REFEICOES, ARQUIVO_JRXML_REFEICOES, usuarioId, dataInicial, dataFinal);
        } catch (Exception e) {
            LOGGER.error("*** ERRO CRÍTICO NO RELATÓRIO REFEIÇÕES ***", e);
            throw e;
        }
    }

    private byte[] gerarRelatorio(String caminhoJasper, String caminhoJrxml, Long usuarioId, Date dataInicial, Date dataFinal) {
        Connection connection = null;

        LOGGER.info("Tentando gerar relatório: {}", caminhoJasper);

        try {
            // 1. Testar conexão
            connection = testarConexao();
            LOGGER.info("✅ Conexão com banco OK");

            // 2. Preparar parâmetros
            Map<String, Object> params = prepararParametros(usuarioId, dataInicial, dataFinal);
            LOGGER.info("✅ Parâmetros preparados");

            // 3. Carregar relatório
            JasperReport report = carregarRelatorioCompilado(caminhoJasper, caminhoJrxml);
            LOGGER.info("✅ Relatório carregado/compilado");

            // 4. Preencher relatório
            LOGGER.info("Preenchendo relatório com dados...");
            JasperPrint print = JasperFillManager.fillReport(report, params, connection);
            LOGGER.info("✅ Relatório preenchido - Páginas: {}", print.getPages().size());

            // 5. Exportar para PDF
            LOGGER.info("Exportando para PDF...");
            byte[] pdf = JasperExportManager.exportReportToPdf(print);
            LOGGER.info("✅ PDF gerado - Tamanho: {} bytes", pdf.length);

            return pdf;

        } catch (JRException e) {
            LOGGER.error("*** ERRO JASPERREPORTS ***");
            LOGGER.error("Código: {}", e.getMessage());
            LOGGER.error("Mensagem: {}", e.getMessage());
            if (e.getCause() != null) {
                LOGGER.error("Causa: {}", e.getCause().getMessage());
            }
            throw new RuntimeException("Erro JasperReports: " + e.getMessage(), e);

        } catch (SQLException e) {
            LOGGER.error("*** ERRO BANCO DE DADOS ***");
            LOGGER.error("SQL State: {}", e.getSQLState());
            LOGGER.error("Error Code: {}", e.getErrorCode());
            LOGGER.error("Mensagem: {}", e.getMessage());
            throw new RuntimeException("Erro banco de dados: " + e.getMessage(), e);

        } catch (Exception e) {
            LOGGER.error("*** ERRO INESPERADO ***");
            LOGGER.error("Tipo: {}", e.getClass().getName());
            LOGGER.error("Mensagem: {}", e.getMessage());
            LOGGER.error("Stack trace completo:", e);
            throw new RuntimeException("Erro inesperado: " + e.getMessage(), e);

        } finally {
            fecharConexao(connection);
        }
    }

    private Connection testarConexao() throws SQLException {
        Connection connection = dataSource.getConnection();

        // Log adicional para debug
        LOGGER.info("🔍 Informações da conexão:");
        LOGGER.info(" - URL: {}", connection.getMetaData().getURL());
        LOGGER.info(" - Database: {}", connection.getMetaData().getDatabaseProductName());
        LOGGER.info(" - Versão: {}", connection.getMetaData().getDatabaseProductVersion());

        if (connection.isValid(2)) {
            LOGGER.info("✅ Conexão válida e funcionando");
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

        LOGGER.debug("Parâmetros detalhados:");
        LOGGER.debug(" - P_USUARIO_ID: {}", usuarioId);
        LOGGER.debug(" - P_DATA_INICIAL: {}", dataInicial);
        LOGGER.debug(" - P_DATA_FINAL: {}", dataFinal);

        return params;
    }

    private JasperReport carregarRelatorioCompilado(String caminhoJasper, String caminhoJrxml) {
        LOGGER.info("Tentando carregar relatório compilado: {}", caminhoJasper);

        try {
            ClassPathResource resource = new ClassPathResource(caminhoJasper);
            LOGGER.info("🔍 Caminho do resource: {}", resource.getPath());
            LOGGER.info("🔍 Resource existe: {}", resource.exists());

            if (!resource.exists()) {
                LOGGER.warn("Arquivo .jasper não encontrado: {}", caminhoJasper);
                LOGGER.info("Tentando compilar do .jrxml: {}", caminhoJrxml);
                return compilarDoJrxml(caminhoJrxml);
            }

            InputStream jasperStream = resource.getInputStream();
            JasperReport report = (JasperReport) JRLoader.loadObject(jasperStream);
            jasperStream.close();

            LOGGER.info("✅ Relatório .jasper carregado com sucesso: {}", caminhoJasper);
            return report;

        } catch (Exception e) {
            LOGGER.warn("❌ Não foi possível carregar .jasper: {}", e.getMessage());
            LOGGER.info("Tentando compilar do .jrxml: {}", caminhoJrxml);
            return compilarDoJrxml(caminhoJrxml);
        }
    }

    private JasperReport compilarDoJrxml(String caminhoJrxml) {
        LOGGER.info("Compilando relatório do .jrxml: {}", caminhoJrxml);

        try {
            ClassPathResource resource = new ClassPathResource(caminhoJrxml);
            LOGGER.info("🔍 Caminho do resource .jrxml: {}", resource.getPath());
            LOGGER.info("🔍 Resource .jrxml existe: {}", resource.exists());

            if (!resource.exists()) {
                throw new RuntimeException("Arquivo .jrxml não encontrado: " + caminhoJrxml);
            }

            InputStream jrxmlStream = resource.getInputStream();
            JasperReport report = JasperCompileManager.compileReport(jrxmlStream);
            jrxmlStream.close();

            LOGGER.info("✅ Relatório compilado com sucesso do .jrxml: {}", caminhoJrxml);
            return report;

        } catch (Exception e) {
            LOGGER.error("❌ ERRO CRÍTICO ao compilar relatório: {}", caminhoJrxml);
            LOGGER.error("Detalhes do erro:", e);
            throw new RuntimeException("Erro ao compilar relatório: " + caminhoJrxml + " - " + e.getMessage(), e);
        }
    }

    private void fecharConexao(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    LOGGER.debug("🔒 Conexão fechada com sucesso");
                }
            } catch (SQLException e) {
                LOGGER.warn("⚠️ Aviso ao fechar conexão: {}", e.getMessage());
            }
        }
    }
}