package com.monitoramento.saude.service;

import java.util.List;
import com.monitoramento.saude.model.Refeicao;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitoramento.saude.dto.RefeicaoResponseDTO;
import com.monitoramento.saude.repository.RefeicaoRepository;

@Service
public class RefeicaoService {

    private final RefeicaoRepository repository;
    private final ObjectMapper objectMapper;

    public RefeicaoService(RefeicaoRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public RefeicaoResponseDTO findRefeicaoById(Long id) {
        return  repository.findById(id)
                    .map(m -> objectMapper.convertValue(m, RefeicaoResponseDTO.class))
                    .orElseThrow(() -> new RuntimeException("Refeição não encontrada!"));
    }

    public List<RefeicaoResponseDTO> findAllRefeicoesById(Long usuarioId) {
        List<Refeicao> refeicoes = repository.findAllByUsuarioId(usuarioId);
        return refeicoes.stream()
                .map(r -> objectMapper.convertValue(r, RefeicaoResponseDTO.class))
                .toList();
    }

    public void createRefeicao(RefeicaoResponseDTO dados) {
        Refeicao refeicao = repository.save(new Refeicao(dados));
        objectMapper.convertValue(refeicao, RefeicaoResponseDTO.class);
    }

    public void deleteRefeicao(Long id) {
        repository.deleteById(id);
    }
}