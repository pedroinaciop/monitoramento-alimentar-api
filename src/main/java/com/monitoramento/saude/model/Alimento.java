package com.monitoramento.saude.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.math.BigDecimal;
import com.monitoramento.saude.enums.UnidadeAlimento;

@Setter
@Getter
@Entity(name = "alimento")
public class Alimento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}