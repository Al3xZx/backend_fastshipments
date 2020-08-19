package com.fastshipmentsdev.backend_fastshipments.d_entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.jdi.JDIPermission;

import javax.persistence.*;

@Entity
@Table(name = "dipendente")
public class Dipendente {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idDipendente;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 150)
    private String cognome;

    @Column(nullable = false, length = 150)
    private String dataNascita;

    @Column(nullable = false, length = 150)
    private String luogoNascita;

    @Column(nullable = false, length = 150)
    private String indirizzo;

    @Column(nullable = false, length = 150)
    private String cf;

    @Column(nullable = false, length = 150)
    private String titoloStudio;

    @Column(nullable = false, length = 150)
    private String mansione;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnore
    private Hub hubLavoro;

    @ManyToOne
    @JoinColumn
    private AreaDiCompetenza areaDiCompetenza;

    public Integer getIdDipendente() {
        return idDipendente;
    }

    public void setIdDipendente(Integer idDipendente) {
        this.idDipendente = idDipendente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getLuogoNascita() {
        return luogoNascita;
    }

    public void setLuogoNascita(String luogoNascita) {
        this.luogoNascita = luogoNascita;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getTitoloStudio() {
        return titoloStudio;
    }

    public void setTitoloStudio(String titoloStudio) {
        this.titoloStudio = titoloStudio;
    }

    public String getMansione() {
        return mansione;
    }

    public void setMansione(String mansione) {
        this.mansione = mansione;
    }

    public Hub getHubLavoro() {
        return hubLavoro;
    }

    public void setHubLavoro(Hub hubLavoro) {
        this.hubLavoro = hubLavoro;
    }

    public AreaDiCompetenza getAreaDiCompetenza() {
        return areaDiCompetenza;
    }

    public void setAreaDiCompetenza(AreaDiCompetenza areaDiCompetenza) {
        this.areaDiCompetenza = areaDiCompetenza;
    }
}
