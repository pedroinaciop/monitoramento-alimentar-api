package com.monitoramento.saude.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import com.monitoramento.saude.enums.UnidadeAlimento;

@Entity(name = "alimento")
public class Alimento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 40)
    private String nomeAlimento;

    @Enumerated(EnumType.STRING)
    private UnidadeAlimento unidadeAlimento;

    private BigDecimal quantidadeAlimento;

    @ManyToOne
    @JoinColumn(name = "refeicao_id")
    @JsonBackReference
    private Refeicao refeicao;

    public Alimento() {}

    public Alimento(Long id, String nomeAlimento, UnidadeAlimento unidadeAlimento, BigDecimal quantidadeAlimento, Refeicao refeicao) {
        this.id = id;
        this.nomeAlimento = nomeAlimento;
        this.unidadeAlimento = unidadeAlimento;
        this.quantidadeAlimento = quantidadeAlimento;
        this.refeicao = refeicao;
    }

    public Long getId() {
        return id;
    }

    public String getNomeAlimento() {
        return nomeAlimento;
    }

    public void setNomeAlimento(String nomeAlimento) {
        this.nomeAlimento = nomeAlimento;
    }

    public UnidadeAlimento getUnidadeAlimento() {
        return unidadeAlimento;
    }

    public void setUnidadeAlimento(UnidadeAlimento unidadeAlimento) {
        this.unidadeAlimento = unidadeAlimento;
    }

    public BigDecimal getQuantidadeAlimento() {
        return quantidadeAlimento;
    }

    public void setQuantidadeAlimento(BigDecimal quantidadeAlimento) {
        this.quantidadeAlimento = quantidadeAlimento;
    }

    public Refeicao getRefeicao() {
        return refeicao;
    }

    public void setRefeicao(Refeicao refeicao) {
        this.refeicao = refeicao;
    }
}