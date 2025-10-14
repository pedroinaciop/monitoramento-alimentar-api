package com.monitoramento.saude.service;

import java.util.List;
import java.util.stream.Collectors;

import com.monitoramento.saude.dto.RefeicaoRequestDTO;
import com.monitoramento.saude.model.Alimento;
import com.monitoramento.saude.model.Refeicao;
import jakarta.transaction.Transactional;
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

    @Transactional
    public RefeicaoResponseDTO createRefeicao(RefeicaoRequestDTO dados) {
        Refeicao refeicao = new Refeicao();
        refeicao.setDataRegistro(dados.dataRegistro());
        refeicao.setTipoRefeicao(dados.tipoRefeicao());
        refeicao.setUsuario(dados.usuario());

        Refeicao finalRefeicao = refeicao;
        refeicao.setAlimentos(
                dados.alimentos().stream().map(a -> {
                    Alimento alimento = new Alimento();
                    alimento.setNomeAlimento(a.nomeAlimento());
                    alimento.setUnidadeAlimento(a.unidadeAlimento());
                    alimento.setQuantidadeAlimento(a.quantidadeAlimento());
                    alimento.setRefeicao(finalRefeicao);
                    return alimento;
                }).collect(Collectors.toList())
        );

        refeicao = repository.save(refeicao);

        return objectMapper.convertValue(refeicao, RefeicaoResponseDTO.class);
    }

    public void deleteRefeicao(Long id) {
        repository.deleteById(id);
    }
}