package com.monitoramento.saude.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitoramento.saude.dto.AlimentoRequestDTO;
import com.monitoramento.saude.dto.AlimentoResponseDTO;
import com.monitoramento.saude.model.Alimento;
import com.monitoramento.saude.repository.AlimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

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

    public ResponseEntity<String> deleteAlimento(Long id) {
        Optional<Alimento> alimento = repository.findById(id);

        if (alimento.isEmpty()) {
            throw new EntityNotFoundException("Alimento não encontrado");
        } else {
            repository.deleteById(id);
            return ResponseEntity.ok("Alimento com o ID " + id + " excluído com sucesso!");
        }
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