package com.monitoramento.saude.service;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitoramento.saude.dto.InfoUserResponseDTO;
import com.monitoramento.saude.model.InfoUsuario;
import com.monitoramento.saude.repository.InfoUsuarioRepository;
import com.monitoramento.saude.repository.MedidasRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import com.monitoramento.saude.dto.InfoUserRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;

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
        repository.deleteById(id);
    }
}
