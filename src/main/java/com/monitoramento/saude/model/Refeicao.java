package com.monitoramento.saude.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.monitoramento.saude.dto.RefeicaoResponseDTO;
import com.monitoramento.saude.enums.TipoRefeicao;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "refeicao")
public class Refeicao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private LocalDateTime dataRegistro;

    @Enumerated(EnumType.STRING)
    private TipoRefeicao tipoRefeicao;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dataAlteracao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    @OneToMany(mappedBy = "refeicao", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Alimento> alimentos = new ArrayList<>();

    public void adicionarAlimento(Alimento alimento) {
        alimento.setRefeicao(this);
        alimentos.add(alimento);
    }

    public Refeicao() {}

    public Refeicao(Long id, LocalDateTime dataRegistro, TipoRefeicao tipoRefeicao, Usuario usuario, List<Alimento> alimentos, LocalDateTime dataAlteracao) {
        this.id = id;
        this.dataRegistro = dataRegistro;
        this.tipoRefeicao = tipoRefeicao;
        this.usuario = usuario;
        this.alimentos = alimentos;
        this.dataAlteracao = dataAlteracao;
    }

    public Refeicao(RefeicaoResponseDTO dados) {
        dataRegistro = dados.dataRegistro();
        tipoRefeicao = dados.tipoRefeicao();
        usuario = dados.usuario();
        alimentos = dados.alimentos();
        dataAlteracao = dados.dataAlteracao();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public TipoRefeicao getTipoRefeicao() {
        return tipoRefeicao;
    }

    public void setTipoRefeicao(TipoRefeicao tipoRefeicao) {
        this.tipoRefeicao = tipoRefeicao;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Alimento> getAlimentos() {
        return alimentos;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public void setAlimentos(List<Alimento> alimentos) {
        this.alimentos = alimentos;
    }
}