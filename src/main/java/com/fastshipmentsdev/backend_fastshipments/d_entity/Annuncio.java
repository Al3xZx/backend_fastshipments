package com.fastshipmentsdev.backend_fastshipments.d_entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "annuncio")
public class Annuncio {

    @Id
    @Column(nullable = false)
    private Integer idAnnuncio;

    @Column(nullable = false)
    private String descrizione;

    @ManyToOne
    @JoinColumn(name = "addetto_risorse_umane", nullable = false)
    private Dipendente addettoUR;

    @OneToMany(mappedBy = "annuncio")
    @JsonIgnore
    private List<Candidato> candidati = new LinkedList<>();

    public Integer getIdAnnuncio() {
        return idAnnuncio;
    }

    public void setIdAnnuncio(Integer idAnnuncio) {
        this.idAnnuncio = idAnnuncio;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Dipendente getAddettoUR() {
        return addettoUR;
    }

    public void setAddettoUR(Dipendente addettoUR) {
        this.addettoUR = addettoUR;
    }

    public List<Candidato> getCandidati() {
        return candidati;
    }

    public void setCandidati(List<Candidato> candidati) {
        this.candidati = candidati;
    }
}
