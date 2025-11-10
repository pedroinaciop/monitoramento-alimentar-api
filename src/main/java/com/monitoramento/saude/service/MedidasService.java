package com.monitoramento.saude.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitoramento.saude.dto.MedidasResponseDTO;
import com.monitoramento.saude.dto.MedidasRequestDTO;
import com.monitoramento.saude.model.Medidas;
import com.monitoramento.saude.repository.MedidasRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class MedidasService {

    private final MedidasRepository repository;
    private final ObjectMapper objectMapper;

    @Autowired
    public MedidasService(MedidasRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public MedidasResponseDTO findMedidasById(Long id) {
        return repository.findById(id)
                .map(m -> objectMapper.convertValue(m, MedidasResponseDTO.class))
                .orElseThrow(() -> new RuntimeException("Medida não encontrada"));
    }

    public List<MedidasResponseDTO> findAllMedidasByUsuarioId(Long usuarioId) {
        List<Medidas> medidas = repository.findAllByUsuarioIdOrderByDataRegistroDesc(usuarioId);
        return medidas.stream()
                .map(m -> objectMapper.convertValue(m, MedidasResponseDTO.class))
                .toList();
    }

    public void createMedida(MedidasRequestDTO dados) {
        Medidas medida = repository.save(new Medidas(dados));
        objectMapper.convertValue(medida, MedidasRequestDTO.class);
    }

    public ResponseEntity<String> deleteMedida(Long id) {
        Optional<Medidas> medidas = repository.findById(id);

        if (medidas.isEmpty()) {
            throw new EntityNotFoundException("Medida não encontrada");
        } else {
            repository.deleteById(id);
            return ResponseEntity.ok("Medida com o ID " + id + " excluída com sucesso");
        }
    }

    public MedidasResponseDTO editarMedidas(Long id, MedidasRequestDTO dados) {
        Medidas medida = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medida não encontrada!"));

        medida.setPesoAtual(dados.pesoAtual());
        medida.setPesoDesejado(dados.pesoDesejado());
        medida.setMedidaCintura(dados.medidaCintura());
        medida.setMedidaQuadril(dados.medidaQuadril());
        medida.setMedidaTorax(dados.medidaTorax());
        medida.setMedidaBracoDireito(dados.medidaBracoDireito());
        medida.setMedidaBracoEsquerdo(dados.medidaBracoEsquerdo());
        medida.setMedidaCoxaDireita(dados.medidaCoxaDireita());
        medida.setMedidaCoxaEsquerda(dados.medidaCoxaEsquerda());
        medida.setMedidaPanturrilhaDireita(dados.medidaPanturrilhaDireita());
        medida.setMedidaPanturrilhaEsquerda(dados.medidaPanturrilhaEsquerda());
        medida.setAltura(dados.altura());
        medida.setDataAlteracao(dados.dataAlteracao());

        Medidas medidaAtualizada = repository.save(medida);
        return objectMapper.convertValue(medidaAtualizada, MedidasResponseDTO.class);
    }

    public BigDecimal calculaIMC(Long usuarioId) {
        return repository.findFirstByUsuarioIdOrderByDataRegistroDesc(usuarioId)
                .map(medidas -> {
                    BigDecimal peso = medidas.getPesoAtual();
                    BigDecimal altura = medidas.getAltura();
                    return peso.divide(altura.multiply(altura),2, RoundingMode.HALF_UP);
                })
                .orElse(BigDecimal.ZERO);
    }

}