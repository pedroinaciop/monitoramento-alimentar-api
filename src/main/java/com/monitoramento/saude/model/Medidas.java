package com.monitoramento.saude.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monitoramento.saude.dto.MedidasRequestDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.math.BigDecimal;

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

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public BigDecimal getPesoAtual() {
        return pesoAtual;
    }

    public void setPesoAtual(BigDecimal pesoAtual) {
        this.pesoAtual = pesoAtual;
    }

    public BigDecimal getPesoDesejado() {
        return pesoDesejado;
    }

    public void setPesoDesejado(BigDecimal pesoDesejado) {
        this.pesoDesejado = pesoDesejado;
    }

    public BigDecimal getMedidaCintura() {
        return medidaCintura;
    }

    public void setMedidaCintura(BigDecimal medidaCintura) {
        this.medidaCintura = medidaCintura;
    }

    public BigDecimal getMedidaQuadril() {
        return medidaQuadril;
    }

    public void setMedidaQuadril(BigDecimal medidaQuadril) {
        this.medidaQuadril = medidaQuadril;
    }

    public BigDecimal getMedidaTorax() {
        return medidaTorax;
    }

    public void setMedidaTorax(BigDecimal medidaTorax) {
        this.medidaTorax = medidaTorax;
    }

    public BigDecimal getMedidaBracoDireito() {
        return medidaBracoDireito;
    }

    public void setMedidaBracoDireito(BigDecimal medidaBracoDireito) {
        this.medidaBracoDireito = medidaBracoDireito;
    }

    public BigDecimal getMedidaBracoEsquerdo() {
        return medidaBracoEsquerdo;
    }

    public void setMedidaBracoEsquerdo(BigDecimal medidaBracoEsquerdo) {
        this.medidaBracoEsquerdo = medidaBracoEsquerdo;
    }

    public BigDecimal getMedidaCoxaDireita() {
        return medidaCoxaDireita;
    }

    public void setMedidaCoxaDireita(BigDecimal medidaCoxaDireita) {
        this.medidaCoxaDireita = medidaCoxaDireita;
    }

    public BigDecimal getMedidaCoxaEsquerda() {
        return medidaCoxaEsquerda;
    }

    public void setMedidaCoxaEsquerda(BigDecimal medidaCoxaEsquerda) {
        this.medidaCoxaEsquerda = medidaCoxaEsquerda;
    }

    public BigDecimal getMedidaPanturrilhaDireita() {
        return medidaPanturrilhaDireita;
    }

    public void setMedidaPanturrilhaDireita(BigDecimal medidaPanturrilhaDireita) {
        this.medidaPanturrilhaDireita = medidaPanturrilhaDireita;
    }

    public BigDecimal getMedidaPanturrilhaEsquerda() {
        return medidaPanturrilhaEsquerda;
    }

    public void setMedidaPanturrilhaEsquerda(BigDecimal medidaPanturrilhaEsquerda) {
        this.medidaPanturrilhaEsquerda = medidaPanturrilhaEsquerda;
    }

    public BigDecimal getAltura() {
        return altura;
    }

    public void setAltura(BigDecimal altura) {
        this.altura = altura;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}