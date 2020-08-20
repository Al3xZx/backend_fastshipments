package com.fastshipmentsdev.backend_fastshipments.d_entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "hub")
public class Hub {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idHub;

    private String nomeCitta;

    private String regione;

    private Double volumeTotale;

    private Double volumeDisponibile;

    @OneToMany(mappedBy = "hub")
    @JsonIgnore
    private List<Scaffale> scaffali = new LinkedList<>();

    @OneToMany(mappedBy = "hubLavoro")
    @JsonIgnore
    private List<Dipendente> dipendenti = new LinkedList<>();

    @OneToMany(mappedBy = "hubPartenza")
    @JsonIgnore
    private List<Spedizione> spedizioniPartenza = new LinkedList<>();

    @OneToMany(mappedBy = "hubDestinazione")
    @JsonIgnore
    private List<Spedizione> spedizioniArrivo = new LinkedList<>();

    @ManyToMany(mappedBy = "hubPassaggio")
    @JsonIgnore
    private List<Spedizione> spedizioniPassaggio = new LinkedList<>();

    public Integer getIdHub() {
        return idHub;
    }

    public void setIdHub(Integer idHub) {
        this.idHub = idHub;
    }

    public String getNomeCitta() {
        return nomeCitta;
    }

    public void setNomeCitta(String nomeCitta) {
        this.nomeCitta = nomeCitta;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public Double getVolumeTotale() {
        return volumeTotale;
    }

    public void setVolumeTotale(Double volumeTotale) {
        this.volumeTotale = volumeTotale;
    }

    public Double getVolumeDisponibile() {
        return volumeDisponibile;
    }

    public void setVolumeDisponibile(Double volumeDisponibile) {
        this.volumeDisponibile = volumeDisponibile;
    }

    public List<Scaffale> getScaffali() {
        return scaffali;
    }

    public void setScaffali(List<Scaffale> scaffali) {
        this.scaffali = scaffali;
    }

    public List<Dipendente> getDipendenti() {
        return dipendenti;
    }

    public void setDipendenti(List<Dipendente> dipendenti) {
        this.dipendenti = dipendenti;
    }

    public List<Spedizione> getSpedizioniPartenza() {
        return spedizioniPartenza;
    }

    public void setSpedizioniPartenza(List<Spedizione> spedizioniPartenza) {
        this.spedizioniPartenza = spedizioniPartenza;
    }

    public List<Spedizione> getSpedizioniArrivo() {
        return spedizioniArrivo;
    }

    public void setSpedizioniArrivo(List<Spedizione> spedizioniArrivo) {
        this.spedizioniArrivo = spedizioniArrivo;
    }

    public List<Spedizione> getSpedizioniPassaggio() {
        return spedizioniPassaggio;
    }

    public void setSpedizioniPassaggio(List<Spedizione> spedizioniPassaggio) {
        this.spedizioniPassaggio = spedizioniPassaggio;
    }
}
