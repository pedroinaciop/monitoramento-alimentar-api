package com.monitoramento.saude.controller;

import com.monitoramento.saude.dto.AlimentoRequestDTO;
import com.monitoramento.saude.dto.AlimentoResponseDTO;
import com.monitoramento.saude.service.AlimentoService;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class AlimentoController {

    private final AlimentoService service;

    public AlimentoController(AlimentoService service) {
        this.service = service;
    }

    @Transactional
    @GetMapping("/alimento/{id}")
    public AlimentoResponseDTO findAlimentoById(@PathVariable("id") Long id) {
        return service.findAlimentoByID(id);
    }

    @Transactional
    @PutMapping("/editar/alimento/{id}")
    public AlimentoResponseDTO editarAlimento(@PathVariable("id") Long id, @RequestBody AlimentoRequestDTO dados) {
        return service.editarAlimento(id, dados);
    }

    @Transactional
    @DeleteMapping("/alimento/{id}")
    public void deleteAlimento(@PathVariable("id") Long id) {
        service.deleteAlimento(id);
    }
}