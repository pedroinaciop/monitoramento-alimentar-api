package com.monitoramento.saude.controller;

import com.monitoramento.saude.dto.EmailDTO;
import com.monitoramento.saude.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/refeicoes")
    public ResponseEntity<Map<String, Object>> debugRefeicoes(@RequestBody EmailDTO dados) {
        Map<String, Object> response = new HashMap<>();

        System.out.println("=== 🚨 DEBUG INICIADO ===");
        System.out.println("Dados recebidos: " + dados);

        try {
            // 1. Testar se o serviço está injetado
            System.out.println("1. EmailService: " + (emailService != null ? "INJETADO" : "NULL"));

            // 2. Testar geração do relatório PRIMEIRO
            System.out.println("2. Testando geração de relatório...");
            // Chame o método do relatorioService diretamente se possível

            // 3. Chamar o metodo real mas com try-catch detalhado
            System.out.println("3. Chamando emailService.sendMailRelatorioRefeicoes...");
            ResponseEntity<String> result = emailService.sendMailRelatorioRefeicoes(dados);

            response.put("success", true);
            response.put("message", result.getBody());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("=== ❌ ERRO CAPTURADO ===");
            System.out.println("Tipo: " + e.getClass().getName());
            System.out.println("Mensagem: " + e.getMessage());
            System.out.println("Stack Trace:");
            e.printStackTrace();

            response.put("success", false);
            response.put("error_type", e.getClass().getName());
            response.put("error_message", e.getMessage());
            response.put("stack_trace", Arrays.toString(e.getStackTrace()));

            return ResponseEntity.status(500).body(response);
        }
    }
}