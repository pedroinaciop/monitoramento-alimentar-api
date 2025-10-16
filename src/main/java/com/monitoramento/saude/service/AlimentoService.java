package com.monitoramento.saude.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitoramento.saude.dto.AlimentoRequestDTO;
import com.monitoramento.saude.dto.AlimentoResponseDTO;
import com.monitoramento.saude.model.Alimento;
import com.monitoramento.saude.repository.AlimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AlimentoService {

    private final AlimentoRepository repository;
    private final ObjectMapper objectMapper;

    public AlimentoService(AlimentoRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public AlimentoResponseDTO findAlimentoByID(Long id) {
        Alimento alimento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alimento não encontrado!"));

        return objectMapper.convertValue(alimento, AlimentoResponseDTO.class);
    }


    public void deleteAlimento(Long id) {
        repository.deleteById(id);
    }

    public AlimentoResponseDTO editarAlimento(Long id, AlimentoRequestDTO dados) {
        Alimento alimento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alimento não encontrado!"));

        alimento.setNomeAlimento(dados.nomeAlimento());
        alimento.setUnidadeAlimento(dados.unidadeAlimento());
        alimento.setQuantidadeAlimento(dados.quantidadeAlimento());

        Alimento alimentoAtualizado = repository.save(alimento);
        return objectMapper.convertValue(alimentoAtualizado, AlimentoResponseDTO.class);
    }
}