package com.monitoramento.saude.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monitoramento.saude.dto.MedidasRequestDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "medidas")
public class Medidas {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private LocalDateTime dataRegistro;

    private BigDecimal pesoAtual;
    private BigDecimal pesoDesejado;

    private BigDecimal medidaCintura;
    private BigDecimal medidaQuadril;
    private BigDecimal medidaTorax;

    private BigDecimal medidaBracoDireito;
    private BigDecimal medidaBracoEsquerdo;

    private BigDecimal medidaCoxaDireita;
    private BigDecimal medidaCoxaEsquerda;

    private BigDecimal medidaPanturrilhaDireita;
    private BigDecimal medidaPanturrilhaEsquerda;

    private BigDecimal altura;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dataAlteracao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    public Medidas() {}

    public Medidas(Long id, LocalDateTime dataRegistro, BigDecimal pesoAtual, BigDecimal pesoDesejado, BigDecimal medidaCintura, BigDecimal medidaQuadril, BigDecimal medidaTorax, BigDecimal medidaBracoDireito, BigDecimal medidaBracoEsquerdo, BigDecimal medidaCoxaDireita, BigDecimal medidaCoxaEsquerda, BigDecimal medidaPanturrilhaDireita, BigDecimal medidaPanturrilhaEsquerda, BigDecimal altura, LocalDateTime dataAlteracao, Usuario usuario) {
        this.id = id;
        this.dataRegistro = dataRegistro;
        this.pesoAtual = pesoAtual;
        this.pesoDesejado = pesoDesejado;
        this.medidaCintura = medidaCintura;
        this.medidaQuadril = medidaQuadril;
        this.medidaTorax = medidaTorax;
        this.medidaBracoDireito = medidaBracoDireito;
        this.medidaBracoEsquerdo = medidaBracoEsquerdo;
        this.medidaCoxaDireita = medidaCoxaDireita;
        this.medidaCoxaEsquerda = medidaCoxaEsquerda;
        this.medidaPanturrilhaDireita = medidaPanturrilhaDireita;
        this.medidaPanturrilhaEsquerda = medidaPanturrilhaEsquerda;
        this.altura = altura;
        this.dataAlteracao = dataAlteracao;
        this.usuario = usuario;
    }

    public Medidas(MedidasRequestDTO dados) {
        dataRegistro = dados.dataRegistro();
        pesoAtual = dados.pesoAtual();
        pesoDesejado = dados.pesoDesejado();
        medidaCintura = dados.medidaCintura();
        medidaQuadril = dados.medidaQuadril();
        medidaTorax = dados.medidaTorax();
        medidaBracoDireito = dados.medidaBracoDireito();
        medidaBracoEsquerdo = dados.medidaBracoEsquerdo();
        medidaCoxaDireita = dados.medidaCoxaDireita();
        medidaCoxaEsquerda = dados.medidaCoxaEsquerda();
        medidaPanturrilhaDireita = dados.medidaPanturrilhaDireita();
        medidaPanturrilhaEsquerda = dados.medidaPanturrilhaEsquerda();
        altura = dados.altura();
        dataAlteracao = dados.dataAlteracao();
        this.usuario = dados.usuario();
    }
}