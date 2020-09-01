package com.fastshipmentsdev.backend_fastshipments.d_entity;

import com.fastshipmentsdev.backend_fastshipments.support.classi.Indirizzo;

import javax.persistence.*;

@Entity
@Table(name = "candidato")
public class Candidato {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idCandidato;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false)
    private String dataNascita;

    @Column(nullable = false)
    private String luogoNascita;

    @Column(nullable = false)
    private String cf;

    @Column(nullable = false)
    private String titoloStudio;

    @Column(nullable = false)
    private String indirizzo;

    @Column(nullable = false)
    @Transient
    private Indirizzo indirizzoCandidato;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Annuncio annuncio;

    public Integer getIdCandidato() {
        return idCandidato;
    }

    public void setIdCandidato(Integer idCandidato) {
        this.idCandidato = idCandidato;
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

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public Annuncio getAnnuncio() {
        return annuncio;
    }

    public void setAnnuncio(Annuncio annuncio) {
        this.annuncio = annuncio;
    }

    public Indirizzo getIndirizzoCandidato() {
        return indirizzoCandidato;
    }

    public void setIndirizzoCandidato(Indirizzo indirizzoCandidato) {
        this.indirizzoCandidato = indirizzoCandidato;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
