package com.fastshipmentsdev.backend_fastshipments.d_entity;

import javax.persistence.*;

@Entity
@Table(name = "merce", uniqueConstraints = @UniqueConstraint(columnNames = {"indice_rotazione", "indice_posizione", "scaffale"}))
public class Merce {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idMerce;

    @Column(nullable = false)
    private Double volume;

    @Column(nullable = false, name = "indice_rotazione")
    private Integer indiceRotazione;

    @Column(nullable = false, name = "indice_posizione")
    private Integer indicePosizione;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Scaffale scaffale;

    @Column(nullable = false)
    private String stato;

    @ManyToOne
    @JoinColumn
    private Spedizione spedizione;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Cliente proprietario;

    public Integer getIdMerce() {
        return idMerce;
    }

    public void setIdMerce(Integer idMerce) {
        this.idMerce = idMerce;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Integer getIndiceRotazione() {
        return indiceRotazione;
    }

    public void setIndiceRotazione(Integer indiceRotazione) {
        this.indiceRotazione = indiceRotazione;
    }

    public Integer getIndicePosizione() {
        return indicePosizione;
    }

    public void setIndicePosizione(Integer indicePosizione) {
        this.indicePosizione = indicePosizione;
    }

    public Scaffale getScaffale() {
        return scaffale;
    }

    public void setScaffale(Scaffale scaffale) {
        this.scaffale = scaffale;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        stato = stato;
    }

    public Spedizione getSpedizione() {
        return spedizione;
    }

    public void setSpedizione(Spedizione spedizione) {
        this.spedizione = spedizione;
    }

    public Cliente getProprietario() {
        return proprietario;
    }

    public void setProprietario(Cliente proprietario) {
        this.proprietario = proprietario;
    }
}
