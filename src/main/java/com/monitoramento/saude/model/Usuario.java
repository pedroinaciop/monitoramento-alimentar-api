package com.monitoramento.saude.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.monitoramento.saude.dto.UserDTO;
import com.monitoramento.saude.enums.UserRole;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Setter
@Getter
@Entity(name = "usuario")
@EqualsAndHashCode(of = "id")
//UserDetails, interface que nos auxilia na administração dos usuários.
public class Usuario implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 80, nullable = false)
    private String nomeCompleto;

    @Column(length = 120, unique = true, nullable = false)
    private String email;

    @Column(length = 60, nullable = false)
    private String senha;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole regra;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dataUpdate;

    @Column(length = 80)
    private String usuarioUpdate;

    @Column(updatable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dataCriacao;

    @Column(length = 80, updatable = false)
    private String usuarioCriacao;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medidas> medidas = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Refeicao> refeicao = new ArrayList<>();

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private InfoUsuario infoUser;

    public Usuario() {}

    public Usuario(Long id, String nomeCompleto, String email, String senha, UserRole regra, LocalDateTime dataUpdate, String usuarioUpdate, LocalDateTime dataCriacao, String usuarioCriacao) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.senha = senha;
        this.regra = regra;
        this.dataUpdate = dataUpdate;
        this.usuarioUpdate = usuarioUpdate;
        this.dataCriacao = dataCriacao;
        this.usuarioCriacao = usuarioCriacao;
    }

    public Usuario(UserDTO dados) {
        this.id = dados.usuario_id();
        this.nomeCompleto = dados.nomeCompleto();
        this.email = dados.email();
        this.senha = dados.senha();
        this.regra = dados.regra();
        this.dataUpdate = dados.dataUpdate();
        this.usuarioUpdate = dados.usuarioUpdate();
        this.dataCriacao = dados.dataCriacao();
    }

    public Usuario(String fullName, String login, String encryptedPassword, UserRole role, LocalDateTime createDate) {
        this.nomeCompleto = fullName;
        this.email = login;
        this.senha = encryptedPassword;
        this.regra = role;
        this.dataCriacao = createDate;
    }

    public Long getUsuario_id() {
        return id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public UserRole getRegra() {
        return regra;
    }

    public LocalDateTime getDataUpdate() {
        return dataUpdate;
    }

    public String getUsuarioUpdate() {
        return usuarioUpdate;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public String getUsuarioCriacao() {
        return usuarioCriacao;
    }

    //Métodos do UserDetails

    @Override
    public String getUsername() {
        return nomeCompleto;
    }

    //Se for ADMIN, ele tem duas permissões: ROLE_ADMIN e ROLE_USER.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.regra == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}