package com.fastshipmentsdev.backend_fastshipments.d_entity;

import javax.persistence.*;

@Entity
@Table(name = "abbonamento")
public class Abbonamento {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idAbbonamento;

    @Column(nullable = false)
    private Double costo;

    @Column(nullable = false)
    private String descrizione;

    @Column(nullable = false)
    private Integer durata; //numero giorni

    @Column(nullable = false)
    private Integer numeroSpedizioni;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Dipendente direttoreVendite;

    public Integer getIdAbbonamento() {
        return idAbbonamento;
    }

    public void setIdAbbonamento(Integer idAbbonamento) {
        this.idAbbonamento = idAbbonamento;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getDurata() {
        return durata;
    }

    public void setDurata(Integer durata) {
        this.durata = durata;
    }

    public Integer getNumeroSpedizioni() {
        return numeroSpedizioni;
    }

    public void setNumeroSpedizioni(Integer numeroSpedizioni) {
        this.numeroSpedizioni = numeroSpedizioni;
    }

    public Dipendente getDirettoreVendite() {
        return direttoreVendite;
    }

    public void setDirettoreVendite(Dipendente direttoreVendite) {
        this.direttoreVendite = direttoreVendite;
    }
}
