package com.monitoramento.saude.controller;

import com.monitoramento.saude.repository.AlimentoRepository;
import com.monitoramento.saude.service.AlimentoService;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class AlimentoController {

    private final AlimentoService service;

    public AlimentoController(AlimentoService service) {
        this.service = service;
    }

    @Transactional
    @DeleteMapping("/alimento/{id}")
    public void deleteAlimento(@PathVariable("id") Long id) {
        service.deleteAlimento(id);
    }
}