package com.monitoramento.saude.repository;

import com.monitoramento.saude.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    UserDetails findByEmail(String email);
    Usuario findByEmailIgnoreCase(String email);
}