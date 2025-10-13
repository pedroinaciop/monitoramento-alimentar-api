package com.monitoramento.saude.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monitoramento.saude.dto.RefeicaoResponseDTO;
import com.monitoramento.saude.enums.TipoRefeicao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "refeicao")
public class Refeicao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private LocalDateTime dataRegistro;

    @Enumerated(EnumType.STRING)
    private TipoRefeicao tipoRefeicao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    @OneToMany(mappedBy = "refeicao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alimento> alimentos = new ArrayList<>();

    public Refeicao() {}

    public Refeicao(Long id, LocalDateTime dataRegistro, TipoRefeicao tipoRefeicao, Usuario usuario, List<Alimento> alimentos) {
        this.id = id;
        this.dataRegistro = dataRegistro;
        this.tipoRefeicao = tipoRefeicao;
        this.usuario = usuario;
        this.alimentos = alimentos;
    }

    public Refeicao(RefeicaoResponseDTO dados) {
        dataRegistro = dados.dataRegistro();
        tipoRefeicao = dados.tipoRefeicao();
        usuario = dados.usuario();
        alimentos = dados.alimentos();
    }
}