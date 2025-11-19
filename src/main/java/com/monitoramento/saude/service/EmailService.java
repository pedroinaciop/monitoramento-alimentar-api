package com.monitoramento.saude.service;

import com.monitoramento.saude.dto.EmailDTO;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final RelatorioService relatorioService;

    public EmailService(JavaMailSender mailSender, RelatorioService relatorioService) {
        this.mailSender = mailSender;
        this.relatorioService = relatorioService;
    }

    public ResponseEntity<String> sendMailRelatorioRefeicoes(EmailDTO dados) {
        log.info("=== 🚨 EMAIL SERVICE INICIADO ===");

        try {
            log.info("1. Dados recebidos - To: {}, UserId: {}", dados.to(), dados.usuarioId());

            // Testar JavaMailSender
            log.info("2. JavaMailSender: {}", mailSender != null ? "OK" : "NULL");

            // GERAR RELATÓRIO PRIMEIRO
            log.info("3. Gerando relatório...");
            byte[] relatorio;
            try {
                relatorio = relatorioService.relatorioRefeicoesDownload(
                        dados.usuarioId(), dados.dataInicial(), dados.dataFinal());
                log.info("4. Relatório gerado - Tamanho: {} bytes", relatorio.length);
            } catch (Exception e) {
                log.error("❌ ERRO NO RELATÓRIO: {}", e.getMessage(), e);
                return ResponseEntity.status(500).body("Erro relatório: " + e.getMessage());
            }

            // PREPARAR EMAIL
            log.info("5. Preparando email...");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("monitoramento.alimentar0@gmail.com");
            helper.setTo(dados.to());
            helper.setSubject(dados.subject());
            helper.setText(dados.body(), true);

            ByteArrayResource resource = new ByteArrayResource(relatorio);
            helper.addAttachment("relatorio-refeicoes.pdf", resource);

            // ENVIAR EMAIL
            log.info("6. Enviando email...");
            mailSender.send(mimeMessage);

            log.info("✅ EMAIL ENVIADO COM SUCESSO");
            return ResponseEntity.ok("E-mail enviado com sucesso");

        } catch (Exception e) {
            log.error("❌ ERRO CRÍTICO NO EMAIL SERVICE");
            log.error("Tipo: {}", e.getClass().getName());
            log.error("Mensagem: {}", e.getMessage());
            log.error("Stack Trace completo:", e);

            return ResponseEntity.status(500)
                    .body("Erro interno: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }
}
