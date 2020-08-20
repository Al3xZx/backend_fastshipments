package com.fastshipmentsdev.backend_fastshipments.d_entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "abbonamento_sottoscritto")
public class AbbonamentoSottoscritto {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idAbbonamentoSottoscritto;

    @Column(nullable = false, length = 500)
    private LocalDateTime dataInizio = LocalDateTime.now();

    @Column(nullable = false, length = 500)
    private LocalDateTime dataFine;

    @Column(nullable = false)
    private Integer spedizioniEffettuate = 0;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Abbonamento abbonamento;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Fattura fattura;

    public Integer getIdAbbonamentoSottoscritto() {
        return idAbbonamentoSottoscritto;
    }

    public void setIdAbbonamentoSottoscritto(Integer idAbbonamentoSottoscritto) {
        this.idAbbonamentoSottoscritto = idAbbonamentoSottoscritto;
    }

    public LocalDateTime getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDateTime dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDateTime getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDateTime dataFine) {
        this.dataFine = dataFine;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Abbonamento getAbbonamento() {
        return abbonamento;
    }

    public void setAbbonamento(Abbonamento abbonamento) {
        this.abbonamento = abbonamento;
    }

    public Fattura getFattura() {
        return fattura;
    }

    public void setFattura(Fattura fattura) {
        this.fattura = fattura;
    }

    public Integer getSpedizioniEffettuate() {
        return spedizioniEffettuate;
    }

    public void setSpedizioniEffettuate(Integer spedizioniEffettuate) {
        this.spedizioniEffettuate = spedizioniEffettuate;
    }
}
