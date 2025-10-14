package com.monitoramento.saude.controller;

import com.monitoramento.saude.dto.RefeicaoRequestDTO;
import com.monitoramento.saude.dto.RefeicaoResponseDTO;
import com.monitoramento.saude.service.RefeicaoService;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class RefeicaoController {

    private final RefeicaoService service;

    public RefeicaoController(RefeicaoService service) {
        this.service = service;
    }

    @GetMapping("/refeicao/{id}")
    public RefeicaoResponseDTO findRefeicaoById(@PathVariable("id") Long id) {
        return service.findRefeicaoById(id);
    }

    @GetMapping("/refeicoes/{id}")
    public List<RefeicaoResponseDTO> findAllRefeicoesById(@PathVariable("id") Long id) {
        return service.findAllRefeicoesById(id);
    }

    @Transactional
    @PostMapping("/cadastro/refeicao/novo")
    public void createRefeicao(@RequestBody RefeicaoRequestDTO dados) {
        service.createRefeicao(dados);
    }

    @Transactional
    @DeleteMapping("/refeicao/{id}")
    public void deleteRefeicao(@PathVariable("id") Long id) {
        service.deleteRefeicao(id);
    }
}