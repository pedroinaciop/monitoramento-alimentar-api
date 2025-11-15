package com.monitoramento.saude.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monitoramento.saude.enums.NivelAtividadeFisica;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.monitoramento.saude.dto.InfoUserRequestDTO;
import com.monitoramento.saude.enums.Sexo;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "info_usuario")
public class InfoUsuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    @Column(length = 3)
    private Integer idade;

    @Enumerated(EnumType.STRING)
    private Sexo sexoBiologico;

    @Enumerated(EnumType.STRING)
    private NivelAtividadeFisica nivelAtividadeFisica;

    @Column(length = 255)
    private String objetivo;

    @Column(length = 500)
    private String alergias;

    @Column(length = 500)
    private String intolerancias;

    @Column(length = 500)
    private String doencasPreExistentes;

    @Column(length = 50)
    private String caracteristicaAlimentar;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dataAlteracao;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private LocalDateTime dataRegistro;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    public InfoUsuario() {}

    public InfoUsuario(Long id, LocalDateTime dataRegistro, LocalDate dataNascimento, Integer idade, Sexo sexoBiologico, NivelAtividadeFisica nivelAtividadeFisica, String objetivo, String alergias, String intolerancias, String doencasPreExistentes, String caracteristicaAlimentar, LocalDateTime dataAlteracao, Usuario usuario) {
        this.id = id;
        this.dataRegistro = dataRegistro;
        this.dataNascimento = dataNascimento;
        this.idade = idade;
        this.sexoBiologico = sexoBiologico;
        this.nivelAtividadeFisica = nivelAtividadeFisica;
        this.objetivo = objetivo;
        this.alergias = alergias;
        this.intolerancias = intolerancias;
        this.doencasPreExistentes = doencasPreExistentes;
        this.dataAlteracao = dataAlteracao;
        this.usuario = usuario;
        this.caracteristicaAlimentar = caracteristicaAlimentar;
    }

    public InfoUsuario(InfoUserRequestDTO dados) {
        dataRegistro = dados.dataRegistro();
        dataNascimento = dados.dataNascimento();
        idade = dados.idade();
        sexoBiologico = dados.sexoBiologico();
        nivelAtividadeFisica = dados.nivelAtividadeFisica();
        objetivo = dados.objetivo();
        alergias = dados.alergias();
        intolerancias = dados.intolerancias();
        doencasPreExistentes = dados.doencasPreExistentes();
        usuario = dados.usuario();
        caracteristicaAlimentar = dados.caracteristicaAlimentar();
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public void setSexoBiologico(Sexo sexoBiologico) {
        this.sexoBiologico = sexoBiologico;
    }

    public void setNivelAtividadeFisica(NivelAtividadeFisica nivelAtividadeFisica) {
        this.nivelAtividadeFisica = nivelAtividadeFisica;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public void setIntolerancias(String intolerancias) {
        this.intolerancias = intolerancias;
    }

    public void setDoencasPreExistentes(String doencasPreExistentes) {
        this.doencasPreExistentes = doencasPreExistentes;
    }

    public void setCaracteristicaAlimentar(String caracteristicaAlimentar) {
        this.caracteristicaAlimentar = caracteristicaAlimentar;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}