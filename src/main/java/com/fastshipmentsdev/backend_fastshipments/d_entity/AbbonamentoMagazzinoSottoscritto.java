package com.fastshipmentsdev.backend_fastshipments.d_entity;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "abbonamento_magazzino_sottoscritto")
public class AbbonamentoMagazzinoSottoscritto {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idAbbonamento;

    @Column(nullable = false, length = 500)
    private LocalDateTime dataInizio = LocalDateTime.now();

    @Column(nullable = false, length = 500)
    private LocalDateTime dataFine;

    @Column(nullable = false)
    private Double volumeUtilizzato = 0.0;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(nullable = false)
    private AbbonamentoMagazzino abbonamentoMagazzino;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Hub hub;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Fattura fattura;

    public Integer getIdAbbonamento() {
        return idAbbonamento;
    }

    public void setIdAbbonamento(Integer idAbbonamento) {
        this.idAbbonamento = idAbbonamento;
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

    public AbbonamentoMagazzino getAbbonamentoMagazzino() {
        return abbonamentoMagazzino;
    }

    public void setAbbonamentoMagazzino(AbbonamentoMagazzino abbonamentoMagazzino) {
        this.abbonamentoMagazzino = abbonamentoMagazzino;
    }

    public Hub getHub() {
        return hub;
    }

    public void setHub(Hub hub) {
        this.hub = hub;
    }

    public Fattura getFattura() {
        return fattura;
    }

    public void setFattura(Fattura fattura) {
        this.fattura = fattura;
    }

    public Double getVolumeUtilizzato() {
        return volumeUtilizzato;
    }

    public void setVolumeUtilizzato(Double volumeUtilizzato) {
        this.volumeUtilizzato = volumeUtilizzato;
    }
}
