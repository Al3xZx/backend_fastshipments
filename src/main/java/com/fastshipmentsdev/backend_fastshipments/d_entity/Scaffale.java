package com.fastshipmentsdev.backend_fastshipments.d_entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Scaffale {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idScaffale;

    private Integer numeroLivelli;

    private Integer qtaIndiciPos;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Hub hub;

    @OneToMany(mappedBy = "scaffale")
    private List<Merce> merci = new LinkedList<>();

    public Integer getIdScaffale() {
        return idScaffale;
    }

    public void setIdScaffale(Integer idScaffale) {
        this.idScaffale = idScaffale;
    }

    public Integer getNumeroLivelli() {
        return numeroLivelli;
    }

    public void setNumeroLivelli(Integer numeroLivelli) {
        this.numeroLivelli = numeroLivelli;
    }

    public Integer getQtaIndiciPos() {
        return qtaIndiciPos;
    }

    public void setQtaIndiciPos(Integer qtaIndiciPos) {
        this.qtaIndiciPos = qtaIndiciPos;
    }

    public Hub getHub() {
        return hub;
    }

    public void setHub(Hub hub) {
        this.hub = hub;
    }

    public List<Merce> getMerci() {
        return merci;
    }

    public void setMerci(List<Merce> merci) {
        this.merci = merci;
    }
}
