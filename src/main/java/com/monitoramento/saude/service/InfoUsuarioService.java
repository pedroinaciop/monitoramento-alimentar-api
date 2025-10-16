package com.monitoramento.saude.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitoramento.saude.dto.InfoUserResponseDTO;
import com.monitoramento.saude.model.InfoUsuario;
import com.monitoramento.saude.repository.InfoUsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import com.monitoramento.saude.dto.InfoUserRequestDTO;

@Service
public class InfoUsuarioService {

    private final InfoUsuarioRepository repository;
    private final ObjectMapper objectMapper;

    public InfoUsuarioService(InfoUsuarioRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public InfoUsuario findInfoUserByUserId(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public void createInfoUser(InfoUserRequestDTO dados) {
        InfoUsuario infoUsuario = repository.save(new InfoUsuario(dados));
        objectMapper.convertValue(infoUsuario, InfoUserRequestDTO.class);
    }

    public void deleteInfoUser(Long id) {
        repository.deleteAll();
    }

    public InfoUserResponseDTO editarInfoUser(Long id, InfoUserRequestDTO dados) {
        InfoUsuario infoUsuario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Informação do usuário não encontrada!"));

        infoUsuario.setDataNascimento(dados.dataNascimento());
        infoUsuario.setIdade(dados.idade());
        infoUsuario.setSexoBiologico(dados.sexoBiologico());
        infoUsuario.setNivelAtividadeFisica(dados.nivelAtividadeFisica());
        infoUsuario.setObjetivo(dados.objetivo());
        infoUsuario.setAlergias(dados.alergias());
        infoUsuario.setIntolerancias(dados.intolerancias());
        infoUsuario.setDoencasPreExistentes(dados.doencasPreExistentes());
        infoUsuario.setDataAlteracao(dados.dataAlteracao());

        InfoUsuario infoUsuarioAtualizado =  repository.save(infoUsuario);
        return objectMapper.convertValue(infoUsuarioAtualizado, InfoUserResponseDTO.class);
    }
}
