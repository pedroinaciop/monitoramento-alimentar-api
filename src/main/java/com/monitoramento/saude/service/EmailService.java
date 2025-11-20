package com.monitoramento.saude.service;

import com.monitoramento.saude.dto.EmailDTO;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${sendgrid.api.key:}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email:no-reply@monitoramento-saude.com}")
    private String fromEmail;

    private final RelatorioService relatorioService;
    private final SimpleDateFormat dateFormatter;

    public EmailService(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
        this.dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    }

    private String carregarTemplate(String caminhoTemplate) throws IOException {
        logger.info("Carregando template: {}", caminhoTemplate);
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(caminhoTemplate);
            if (inputStream == null) {
                throw new IOException("Template não encontrado: " + caminhoTemplate);
            }
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

            byte[] relatorio = relatorioService.relatorioRefeicoesDownload(
                    dados.usuarioId(), dados.dataInicial(), dados.dataFinal());

            enviarViaSendGrid(dados.to(),
                    dados.subject() != null ? dados.subject() : "Relatório de Refeições",
                    html, relatorio, "relatorio-refeicoes.pdf");

            logger.info("E-mail de relatório de refeições enviado com sucesso para: {}", dados.to());
            return ResponseEntity.ok("E-mail enviado com sucesso");

        } catch (Exception e) {
            logger.error("Erro ao enviar e-mail de relatório de refeições para: {}", dados.to(), e);
            return ResponseEntity.internalServerError().body("Erro ao enviar e-mail: " + e.getMessage());
        }
    }

    public ResponseEntity<String> sendMailRelatorioMedidas(EmailDTO dados) {
        try {
            logger.info("Enviando e-mail de relatório de medidas para: {}", dados.to());

            String html = carregarTemplate("templates/EmailMedidas.html");
            html = processarTemplate(html, dados);

            byte[] relatorio = relatorioService.relatorioMedidasDownload(
                    dados.usuarioId(), dados.dataInicial(), dados.dataFinal());

            enviarViaSendGrid(dados.to(),
                    dados.subject() != null ? dados.subject() : "Relatório de Medidas",
                    html, relatorio, "relatorio-medidas.pdf");

            logger.info("E-mail de relatório de medidas enviado com sucesso para: {}", dados.to());
            return ResponseEntity.ok("E-mail enviado com sucesso");

        } catch (Exception e) {
            logger.error("Erro ao enviar e-mail de relatório de medidas para: {}", dados.to(), e);
            return ResponseEntity.internalServerError().body("Erro ao enviar e-mail: " + e.getMessage());
        }
    }

    private void enviarViaSendGrid(String to, String subject, String htmlContent,
                                   byte[] attachment, String attachmentName) throws IOException {
        // Verifica se a API Key está configurada
        if (sendGridApiKey == null || sendGridApiKey.trim().isEmpty()) {
            throw new IOException("SendGrid API Key não configurada");
        }

        Email from = new Email(fromEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, toEmail, content);

        // Adiciona anexo se existir
        if (attachment != null && attachment.length > 0) {
            Attachments attachments = new Attachments();
            String base64Content = Base64.getEncoder().encodeToString(attachment);
            attachments.setContent(base64Content);
            attachments.setType("application/pdf");
            attachments.setFilename(attachmentName);
            attachments.setDisposition("attachment");
            mail.addAttachments(attachments);
            logger.info("Anexo {} adicionado ao e-mail (tamanho: {} bytes)", attachmentName, attachment.length);
        }

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            int statusCode = response.getStatusCode();
            logger.info("SendGrid Response - Status: {}, Body: {}", statusCode, response.getBody());

            if (statusCode >= 400) {
                throw new IOException("SendGrid API error: " + statusCode + " - " + response.getBody());
            }

        } catch (IOException ex) {
            logger.error("Erro na chamada da SendGrid API", ex);
            throw ex;
        }
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