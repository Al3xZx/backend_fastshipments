package com.fastshipmentsdev.backend_fastshipments.d_entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fastshipmentsdev.backend_fastshipments.support.classi.Indirizzo;
import org.w3c.dom.ls.LSProgressEvent;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * un’entità spedizione identificata da idSpedizione e caratterizzata da hub di partenza, lista
 * delle merci, data di partenza, data di arrivo, dal cliente mittente, indirizzo del destinatario,
 * hub di destinazione, una lista di hub per i quali passa, la lista di trasportatori extraurbani che
 * la trasportano, il trasportatore urbano che la ritira, il trasportatore urbano che la consegna,
 * volume complessivo e peso tassabile (= volume complessivo *300)
 *
 * spedizione (idSpedizione, dataPartenza, dataArrivo, indirizzoDestinatario, mittente, traspRitiro, traspConsegna,
 *              hubPartenza, hubDestinazione, volume, pesoTassabile, fattura*)
 * 	spedizione (mittente) ⊑FK cliente (idCliente)
 * 	spedizione (traspRitiro) ⊑FK dipendente (idDipendente)
 * 	spedizione (traspConsegna) ⊑FK dipendente (idDipendente)
 * 	spedizione (hubPartenza) ⊑FK hub (idHub)
 * 	spedizione (hubDestinazione) ⊑FK hub (idHub)
 * 	spedizione (fattura) ⊑FK  fattura (idFattura)
 *
 * spedizione_trasp_extr (spedizione, trasportatore_extraurbano)
 * 	spedizione_trasp_extr (trasportatore_extraurbano) ⊑FK  dipendente (idDipendente)
 *  spedizione_trasp_extr (spedizione) ⊑FK  spedizione (idSpedizione)
 *
 * passaggio_hub (spedizione, hub)
 * 	passaggio_hub (spedizione) ⊑FK  spedizione (idSpedizione)
 * 	passaggio_hub (spedizione) ⊑FK hub(idHub)
 * */

@Entity
@Table(name = "spedizione")
public class Spedizione {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idSpedizione;

    private LocalDateTime dataPartenza;

    private LocalDateTime dataArrivo;

    @Column(nullable = false)
    private String indirizzoDestinatario;

    @Transient
    @JsonInclude
    private Indirizzo indirizzo;

    private String stato;

    @Column(nullable = false)
    private Double volume;

    @Column(nullable = false)
    private Double pesoTassabile;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Cliente mittente; //il cliente che spedisce.

    @ManyToOne
    @JoinColumn
    private Dipendente traspRitiro;

    @ManyToOne
    @JoinColumn
    private Dipendente traspConsegna;

    @ManyToMany
    @JoinTable(
            name = "spedizione_trasp_extr",
            joinColumns = @JoinColumn(name = "spedizione", referencedColumnName = "idSpedizione"),
            inverseJoinColumns = @JoinColumn(name = "rasportatore_extraurbano", referencedColumnName = "idDipendente")
    )
    private List<Dipendente> traspExtraurbano = new LinkedList<>();

    @ManyToOne
    @JoinColumn
    private Hub hubPartenza;

    @ManyToOne
    @JoinColumn
    private Hub hubDestinazione;

    @ManyToMany
    @JoinTable(
            name = "passaggio_hub",
            joinColumns = @JoinColumn(name = "spedizione", referencedColumnName = "idSpedizione"),
            inverseJoinColumns = @JoinColumn(name = "hub", referencedColumnName = "idHub")
    )
    private Set<Hub> hubPassaggio = new TreeSet<>();

    @OneToMany(mappedBy = "spedizione")
    private List<Merce> merci = new LinkedList<>();

    @ManyToOne
    @JoinColumn
    private Fattura fattura;

    public Integer getIdSpedizione() {
        return idSpedizione;
    }

    public void setIdSpedizione(Integer idSpedizione) {
        this.idSpedizione = idSpedizione;
    }

    public LocalDateTime getDataPartenza() {
        return dataPartenza;
    }

    public void setDataPartenza(LocalDateTime dataPartenza) {
        this.dataPartenza = dataPartenza;
    }

    public LocalDateTime getDataArrivo() {
        return dataArrivo;
    }

    public void setDataArrivo(LocalDateTime dataArrivo) {
        this.dataArrivo = dataArrivo;
    }

    public String getIndirizzoDestinatario() {
        return indirizzoDestinatario;
    }

    public void setIndirizzoDestinatario(String indirizzoDestinatario) {
        this.indirizzoDestinatario = indirizzoDestinatario;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getPesoTassabile() {
        return pesoTassabile;
    }

    public void setPesoTassabile(Double pesoTassabile) {
        this.pesoTassabile = pesoTassabile;
    }

    public Cliente getMittente() {
        return mittente;
    }

    public void setMittente(Cliente mittente) {
        this.mittente = mittente;
    }

    public Dipendente getTraspRitiro() {
        return traspRitiro;
    }

    public void setTraspRitiro(Dipendente traspRitiro) {
        this.traspRitiro = traspRitiro;
    }

    public Dipendente getTraspConsegna() {
        return traspConsegna;
    }

    public void setTraspConsegna(Dipendente traspConsegna) {
        this.traspConsegna = traspConsegna;
    }

    public List<Dipendente> getTraspExtraurbano() {
        return traspExtraurbano;
    }

    public void setTraspExtraurbano(List<Dipendente> traspExtraurbano) {
        this.traspExtraurbano = traspExtraurbano;
    }

    public Hub getHubPartenza() {
        return hubPartenza;
    }

    public void setHubPartenza(Hub hubPartenza) {
        this.hubPartenza = hubPartenza;
    }

    public Hub getHubDestinazione() {
        return hubDestinazione;
    }

    public void setHubDestinazione(Hub hubDestinazione) {
        this.hubDestinazione = hubDestinazione;
    }

    public Set<Hub> getHubPassaggio() {
        return hubPassaggio;
    }

    public void setHubPassaggio(Set<Hub> hubPassaggio) {
        this.hubPassaggio = hubPassaggio;
    }

    public Fattura getFattura() {
        return fattura;
    }

    public void setFattura(Fattura fattura) {
        this.fattura = fattura;
    }

    public Indirizzo getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(Indirizzo indirizzo) {
        this.indirizzo = indirizzo;
    }

    public List<Merce> getMerci() {
        return merci;
    }

    public void setMerci(List<Merce> merci) {
        this.merci = merci;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}
