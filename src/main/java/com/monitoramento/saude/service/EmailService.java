package com.monitoramento.saude.service;

import com.monitoramento.saude.dto.EmailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final RelatorioService relatorioService;

    public EmailService(JavaMailSender mailSender, RelatorioService relatorioService) {
        this.mailSender = mailSender;
        this.relatorioService = relatorioService;
    }

    public ResponseEntity<String> sendMailRelatorioMedidas(EmailDTO dados) throws MessagingException, IOException {
        String html;
        ClassPathResource resource = new ClassPathResource("templates/EmailMedidas.html");
        html = Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(dados.to().toLowerCase());
        helper.setSubject(dados.subject());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        html = html.replace("${to}",dados.to().toLowerCase());
        html = html.replace("${dataInicial}", sdf.format(dados.dataInicial()));
        html = html.replace("${dataFinal}", sdf.format(dados.dataFinal()));

        byte[] relatorio = relatorioService.relatorioMedidasDownload(dados.usuarioId(), dados.dataInicial(), dados.dataFinal());
        ByteArrayResource resourceGerado = new ByteArrayResource(relatorio);
        helper.addAttachment("relatorio-medidas.pdf", resourceGerado);

        helper.setText(html, true);
        mailSender.send(mimeMessage);
        return ResponseEntity.ok("E-mail enviado com sucesso");
    }

    public ResponseEntity<String> sendMailRelatorioRefeicoes(EmailDTO dados) throws MessagingException, IOException {
        String html;
        ClassPathResource resource = new ClassPathResource("templates/EmailRefeicoes.html");
        html = Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(dados.to().toLowerCase());
        helper.setSubject(dados.subject());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        html = html.replace("${to}",dados.to().toLowerCase());
        html = html.replace("${dataInicial}", sdf.format(dados.dataInicial()));
        html = html.replace("${dataFinal}", sdf.format(dados.dataFinal()));

        byte[] relatorio = relatorioService.relatorioRefeicoesDownload(dados.usuarioId(), dados.dataInicial(), dados.dataFinal());
        ByteArrayResource relatorioGerado = new ByteArrayResource(relatorio);
        helper.addAttachment("relatorio-refeicoes.pdf", relatorioGerado);

        helper.setText(html, true);
        mailSender.send(mimeMessage);
        return ResponseEntity.ok("E-mail enviado com sucesso");
    }
}
