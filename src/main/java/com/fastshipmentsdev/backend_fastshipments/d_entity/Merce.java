package com.fastshipmentsdev.backend_fastshipments.d_entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
//@Table(name = "merce", uniqueConstraints = @UniqueConstraint(columnNames = {"indice_rotazione", "indice_posizione", "scaffale"}))
@Table(name = "merce")
public class Merce {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idMerce;

    @Column(nullable = false)
    private Double volume;//VOLUME OCCUPATO

    @Column(nullable = false)
    private String descrizione;

    @Column(nullable = true, name = "indice_rotazione")
    private Integer indiceRotazione;

    @Column(nullable = true, name = "indice_posizione")
    private Integer indicePosizione;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Scaffale scaffale;

    @Column(nullable = false)
    private String stato;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Spedizione spedizione;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Cliente proprietario;

    public Merce() {
    }

    public Merce(Merce merce) {
        this.volume = merce.volume;
        this.descrizione = merce.descrizione;
        this.indiceRotazione = merce.indiceRotazione;
        this.indicePosizione = merce.indicePosizione;
        this.scaffale = merce.scaffale;
        this.stato = merce.stato;
        this.spedizione = merce.spedizione;
        this.proprietario = merce.proprietario;
    }

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
        this.stato = stato;
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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public String toString() {
        return "Merce{" +
                "idMerce=" + idMerce +
                ", volume=" + volume +
                ", descrizione='" + descrizione + '\'' +
                ", indiceRotazione=" + indiceRotazione +
                ", indicePosizione=" + indicePosizione +
                ", scaffale=" + scaffale +
                ", stato='" + stato + '\'' +
                ", spedizione=" + spedizione +
                ", proprietario=" + proprietario +
                '}';
    }
}
