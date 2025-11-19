package com.monitoramento.saude.controller;

import com.monitoramento.saude.dto.EmailDTO;
import com.monitoramento.saude.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email/")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/medidas")
    public ResponseEntity<String> sendMailRelatorioMedidas(@RequestBody EmailDTO dados) throws MessagingException {
        return emailService.sendMailRelatorioMedidas(dados);
    }

    @PostMapping("/refeicoes")
    public ResponseEntity<String> sendMailRelatorioRefeicoes(@RequestBody EmailDTO dados) throws MessagingException {
        return emailService.sendMailRelatorioRefeicoes(dados);
    }
}