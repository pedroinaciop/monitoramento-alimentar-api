package com.monitoramento.saude.controller;

import java.math.BigDecimal;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.monitoramento.saude.dto.MedidasRequestDTO;
import com.monitoramento.saude.service.MedidasService;
import com.monitoramento.saude.dto.MedidasResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/")
public class MedidasController {

    @Autowired
    public MedidasService service;

    @GetMapping("/medida/{id}")
    public MedidasResponseDTO findMedidasById(@PathVariable("id") Long id) {
        return service.findMedidasById(id);
    }

    @GetMapping("/medidas/{id}")
    public List<MedidasResponseDTO> findAllMedidasByUsuarioId(@PathVariable("id") Long id) {
        return service.findAllMedidasByUsuarioId(id);
    }

    @GetMapping("/medidas/imc/{id}")
    public BigDecimal getIMC(@PathVariable("id") Long id) {
        return service.calculaIMC(id);
    }

    @Transactional
    @PostMapping("/cadastros/medida/novo")
    public void createMedida(@RequestBody MedidasRequestDTO dados) {
        service.createMedida(dados);
    }

    @Transactional
    @PutMapping("/editar/medida/{id}")
    public MedidasResponseDTO editarMedida(@PathVariable("id") Long id, @RequestBody MedidasRequestDTO dados) {
        return service.editarMedidas(id, dados);
    }

    @Transactional
    @DeleteMapping("/medida/{id}")
    public ResponseEntity<String> deleteMedida(@PathVariable("id") Long id ) {
        return service.deleteMedida(id);
    }
}
