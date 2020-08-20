package com.fastshipmentsdev.backend_fastshipments.d_entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fastshipmentsdev.backend_fastshipments.support.classi.Indirizzo;

import javax.persistence.*;
import java.util.*;

/**
 un’entità cliente è identificata da idCliente
 e caratterizzata dagli attributi, telefono, indirizzo di residenza/sede ed e-mail, dalla lista di
 abbonamenti sottoscritti e una lista di fatture a suo carico. Un Cliente si distingue in
 cliente_azienda caratterizzato dalla ragione sociale e partita iva e cliente_privato
 caratterizzato da nome, cognome e codice fiscale*/

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idCliente;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false, length = 150)
    private String indirizzo;

    @Transient
    @JsonInclude
    private Indirizzo indirizzoCliente;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(length = 50)
    private String ragioneSociale;

    private Integer pIva;

    private String nome;

    private String cognome;

    private String cf;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private Set<AbbonamentoSottoscritto> abbonamenti = new TreeSet<>();

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private Set<AbbonamentoMagazzinoSottoscritto> abbonamentiMagazzino = new TreeSet<>();

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private Set<Fattura> fatture = new TreeSet<>();

    @OneToMany(mappedBy = "proprietario")
    @JsonIgnore
    private List<Merce> merceProprietario = new LinkedList<>();

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public Integer getpIva() {
        return pIva;
    }

    public void setpIva(Integer pIva) {
        this.pIva = pIva;
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

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public Set<AbbonamentoSottoscritto> getAbbonamenti() {
        return abbonamenti;
    }

    public void setAbbonamenti(Set<AbbonamentoSottoscritto> abbonamenti) {
        this.abbonamenti = abbonamenti;
    }

    public Set<AbbonamentoMagazzinoSottoscritto> getAbbonamentiMagazzino() {
        return abbonamentiMagazzino;
    }

    public void setAbbonamentiMagazzino(Set<AbbonamentoMagazzinoSottoscritto> abbonamentiMagazzino) {
        this.abbonamentiMagazzino = abbonamentiMagazzino;
    }

    public Set<Fattura> getFatture() {
        return fatture;
    }

    public void setFatture(Set<Fattura> fatture) {
        this.fatture = fatture;
    }

    public List<Merce> getMerceProprietario() {
        return merceProprietario;
    }

    public void setMerceProprietario(List<Merce> merceProprietario) {
        this.merceProprietario = merceProprietario;
    }

    public Indirizzo getIndirizzoCliente() {
        return indirizzoCliente;
    }

    public void setIndirizzoCliente(Indirizzo indirizzoCliente) {
        this.indirizzoCliente = indirizzoCliente;
    }
}
