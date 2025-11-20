package com.monitoramento.saude.service;

import com.monitoramento.saude.dto.EmailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final RelatorioService relatorioService;
    private final SimpleDateFormat dateFormatter;

    public EmailService(JavaMailSender mailSender, RelatorioService relatorioService) {
        this.mailSender = mailSender;
        this.relatorioService = relatorioService;
        this.dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    }

    private String carregarTemplate(String caminhoTemplate) throws IOException {
        logger.info("Carregando template: {}", caminhoTemplate);
        try {
            ClassPathResource resource = new ClassPathResource(caminhoTemplate);
            InputStream inputStream = resource.getInputStream();
            byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Erro ao carregar template: {}", caminhoTemplate, e);
            throw new IOException("Erro ao carregar template: " + caminhoTemplate, e);
        }
    }

    private String processarTemplate(String html, EmailDTO dados) {
        return html.replace("${to}", dados.to())
                .replace("${dataInicial}", dateFormatter.format(dados.dataInicial()))
                .replace("${dataFinal}", dateFormatter.format(dados.dataFinal()));
    }

    public ResponseEntity<String> sendMailRelatorioRefeicoes(EmailDTO dados) {
        try {
            logger.info("Enviando e-mail de relatório de refeições para: {}", dados.to());

            String html = carregarTemplate("templates/EmailRefeicoes.html");
            html = processarTemplate(html, dados);

            MimeMessage mimeMessage = criarMimeMessage(dados, html, "relatorio-refeicoes.pdf");
            mailSender.send(mimeMessage);

            logger.info("E-mail de relatório de refeições enviado com sucesso para: {}", dados.to());
            return ResponseEntity.ok("E-mail enviado com sucesso");

        } catch (MessagingException | IOException e) {
            logger.error("Erro ao enviar e-mail de relatório de refeições para: {}", dados.to(), e);
            return ResponseEntity.internalServerError().body("Erro ao enviar e-mail: " + e.getMessage());
        } catch (MailException e) {
            logger.error("Erro no envio de e-mail para: {}", dados.to(), e);
            return ResponseEntity.internalServerError().body("Erro no envio de e-mail: " + e.getMessage());
        }
    }

    public ResponseEntity<String> sendMailRelatorioMedidas(EmailDTO dados) {
        try {
            logger.info("Enviando e-mail de relatório de medidas para: {}", dados.to());

            String html = carregarTemplate("templates/EmailMedidas.html");
            html = processarTemplate(html, dados);

            MimeMessage mimeMessage = criarMimeMessage(dados, html, "relatorio-medidas.pdf");
            mailSender.send(mimeMessage);

            logger.info("E-mail de relatório de medidas enviado com sucesso para: {}", dados.to());
            return ResponseEntity.ok("E-mail enviado com sucesso");

        } catch (MessagingException | IOException e) {
            logger.error("Erro ao enviar e-mail de relatório de medidas para: {}", dados.to(), e);
            return ResponseEntity.internalServerError().body("Erro ao enviar e-mail: " + e.getMessage());
        } catch (MailException e) {
            logger.error("Erro no envio de e-mail para: {}", dados.to(), e);
            return ResponseEntity.internalServerError().body("Erro no envio de e-mail: " + e.getMessage());
        }
    }

    private MimeMessage criarMimeMessage(EmailDTO dados, String html, String nomeArquivo)
            throws MessagingException, IOException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(dados.to().toLowerCase());
        helper.setSubject(dados.subject() != null ? dados.subject() : "Relatório de Monitoramento");
        helper.setText(html, true);

        // Adiciona anexo baseado no tipo de relatório
        byte[] relatorio = obterRelatorio(dados, nomeArquivo);
        if (relatorio != null && relatorio.length > 0) {
            ByteArrayResource anexo = new ByteArrayResource(relatorio);
            helper.addAttachment(nomeArquivo, anexo);
            logger.info("Anexo {} adicionado ao e-mail (tamanho: {} bytes)", nomeArquivo, relatorio.length);
        } else {
            logger.warn("Relatório vazio ou nulo para: {}", dados.to());
        }

        return mimeMessage;
    }

    private byte[] obterRelatorio(EmailDTO dados, String nomeArquivo) throws IOException {
        if (nomeArquivo.contains("refeicoes")) {
            return relatorioService.relatorioRefeicoesDownload(
                    dados.usuarioId(), dados.dataInicial(), dados.dataFinal());
        } else if (nomeArquivo.contains("medidas")) {
            return relatorioService.relatorioMedidasDownload(
                    dados.usuarioId(), dados.dataInicial(), dados.dataFinal());
        }
        throw new IOException("Tipo de relatório não suportado: " + nomeArquivo);
    }
}