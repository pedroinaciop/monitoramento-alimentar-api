package com.monitoramento.saude.service;

import com.monitoramento.saude.repository.AlimentoRepository;
import org.springframework.stereotype.Service;

@Service
public class AlimentoService {

    private final AlimentoRepository repository;

    public AlimentoService(AlimentoRepository repository) {
        this.repository = repository;
    }

    public void deleteAlimento(Long id) {
        repository.deleteById(id);
    }
}