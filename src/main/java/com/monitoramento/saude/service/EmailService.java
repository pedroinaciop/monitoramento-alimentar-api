package com.monitoramento.saude.service;

import com.monitoramento.saude.dto.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final RelatorioService relatorioService;

    public EmailService(JavaMailSender mailSender, RelatorioService relatorioService) {
        this.mailSender = mailSender;
        this.relatorioService = relatorioService;
    }

    public ResponseEntity<String> sendMailRelatorioMedidas(Email dados) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom("monitoramente_saude@adminpainel.store");
        helper.setTo(dados.to().toLowerCase());
        helper.setSubject(dados.subject());
        helper.setText(dados.body());

        byte[] relatorio = relatorioService.relatorioMedidasDownload(dados.usuarioId(), dados.dataInicial(), dados.dataFinal());

        ByteArrayResource resource = new ByteArrayResource(relatorio);

        helper.addAttachment("relatorio-medidas.pdf", resource);

        mailSender.send(mimeMessage);
        return ResponseEntity.ok("E-mail enviado com sucesso");
    }

    public ResponseEntity<String> sendMailRelatorioRefeicoes(Email dados) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom("monitoramente_saude@adminpainel.store");
        helper.setTo(dados.to().toLowerCase());
        helper.setSubject(dados.subject());
        helper.setText(dados.body());

        byte[] relatorio = relatorioService.relatorioRefeicoesDownload(dados.usuarioId(), dados.dataInicial(), dados.dataFinal());

        ByteArrayResource resource = new ByteArrayResource(relatorio);

        helper.addAttachment("relatorio-refeicoes.pdf", resource);

        mailSender.send(mimeMessage);
        return ResponseEntity.ok("E-mail enviado com sucesso");
    }
}
