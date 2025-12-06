package com.monitoramento.saude.service;

import java.util.List;
import java.util.stream.Collectors;
import com.monitoramento.saude.dto.UserDTO;
import com.monitoramento.saude.model.Usuario;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import com.monitoramento.saude.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final ObjectMapper objectMapper;

    public UserService(UsuarioRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public void registerUser(UserDTO dados) {
        repository.save(new Usuario(dados));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username);
    }


    public UserDTO getUserById(Long id) {
        var user = repository.findById(id);
        return objectMapper.convertValue(user, UserDTO.class);
    }

    public List<UserDTO> getUsers() {
        return convertData(repository.findAll());
    }

    private List<UserDTO> convertData(List<Usuario> users) {
        return users
                .stream()
                .map(u -> new UserDTO(u.getUsuario_id(), u.getNomeCompleto(), u.getEmail(), u.getSenha(), u.getRegra(), u.getDataUpdate(), u.getUsuarioUpdate(), u.getDataCriacao()))
                .collect(Collectors.toList());
    }

    public UserDTO editUser(Long id, UserDTO dados) {
        Usuario user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));

        user.setNomeCompleto(dados.nomeCompleto());
        user.setDataUpdate(dados.dataUpdate());

        if (dados.senha() != null && !dados.senha().isBlank()) {
            String encryptedPassword = new BCryptPasswordEncoder().encode(dados.senha());
            user.setSenha(encryptedPassword);
        }

        Usuario UserAtualizada = repository.save(user);
        return objectMapper.convertValue(UserAtualizada, UserDTO.class);
    }
}