package com.monitoramento.saude.service;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.monitoramento.saude.dto.AlimentoRequestDTO;
import com.monitoramento.saude.dto.RefeicaoRequestDTO;
import com.monitoramento.saude.model.Alimento;
import com.monitoramento.saude.model.Refeicao;
import com.monitoramento.saude.repository.AlimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitoramento.saude.dto.RefeicaoResponseDTO;
import com.monitoramento.saude.repository.RefeicaoRepository;

@Service
public class RefeicaoService {

    private final RefeicaoRepository repository;
    private final AlimentoRepository repositoryAlimento;
    private final ObjectMapper objectMapper;

    public RefeicaoService(RefeicaoRepository repository, AlimentoRepository repositoryAlimento, ObjectMapper objectMapper) {
        this.repository = repository;
        this.repositoryAlimento = repositoryAlimento;
        this.objectMapper = objectMapper;
    }

    public RefeicaoResponseDTO findRefeicaoById(Long id) {
        return  repository.findById(id)
                    .map(m -> objectMapper.convertValue(m, RefeicaoResponseDTO.class))
                    .orElseThrow(() -> new RuntimeException("Refeição não encontrada!"));
    }

    public List<RefeicaoResponseDTO> findAllRefeicoesById(Long usuarioId) {
        List<Refeicao> refeicoes = repository.findAllByUsuarioIdOrderByDataRegistroDesc(usuarioId);
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

    public ResponseEntity<String> deleteRefeicao(Long id) {
        Optional<Refeicao> refeicao = repository.findById(id);

        if (refeicao.isEmpty()) {
            throw new EntityNotFoundException("Refeição não encontrada");
        } else {
            repository.deleteById(id);
            return ResponseEntity.ok("Refeição com o ID " + id + " excluída com sucesso");
        }
    }

    public RefeicaoResponseDTO editarRefeicao(Long id, RefeicaoRequestDTO dados) {
        Refeicao refeicao = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Refeição não encontrada!"));

        refeicao.setDataRegistro(dados.dataRegistro());
        refeicao.setTipoRefeicao(dados.tipoRefeicao());
        List<Alimento> alimentosAtualizados = dados.alimentos().stream().map(a -> {
            Alimento alimento = (a.id() != null)
                    ? repositoryAlimento.findById(a.id()).orElse(new Alimento())
                    : new Alimento();
            alimento.setNomeAlimento(a.nomeAlimento());
            alimento.setUnidadeAlimento(a.unidadeAlimento());
            alimento.setQuantidadeAlimento(a.quantidadeAlimento());
            alimento.setRefeicao(refeicao);
            return alimento;
        }).collect(Collectors.toList());

        refeicao.getAlimentos().clear();
        refeicao.getAlimentos().addAll(alimentosAtualizados);
        refeicao.setDataAlteracao(dados.dataAlteracao());

        Refeicao refeicaoAtualizada = repository.save(refeicao);
        return objectMapper.convertValue(refeicaoAtualizada, RefeicaoResponseDTO.class);
    }
}