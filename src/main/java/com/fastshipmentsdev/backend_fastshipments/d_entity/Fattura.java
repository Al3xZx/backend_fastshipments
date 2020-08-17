package com.fastshipmentsdev.backend_fastshipments.d_entity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**entità fattura identificata da un idFattura
 e caratterizzata dalla data di emissione, da un numero progressivo, dalla lista di
 abbonamenti sottoscritti ai quali si riferisce, lista di spedizioni alle quali si riferisce, il cliente che ha
 effettuato l’acquisto, imponibile, totale (= imponibile +iva)

 fattura(idFattura, dataEmissione, numero, imponibile, totale, cliente)
    fattura(cliente) ⊑FK cliente (idCliente)

 */
@Entity
@Table(name = "fattura")
public class Fattura {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idFattura;

    @Column(nullable = false)
    private Integer numero;

    @Column(nullable = false)
    private Double imponibile;

    @Column(nullable = false)
    private Double totale;

    @OneToMany(mappedBy = "fattura")
    private List<AbbonamentoSottoscritto> abbonamentiSottoscritti = new LinkedList<>();

    @OneToMany(mappedBy = "fattura")
    private List<AbbonamentoMagazzinoSottoscritto> abbonamentiMagazzinoSottoscritti = new LinkedList<>();

    @OneToMany(mappedBy = "fattura")
    private List<Spedizione> spedizioni = new LinkedList<>();

    @ManyToOne
    @JoinColumn(nullable = false)
    private Cliente cliente;

    public Integer getIdFattura() {
        return idFattura;
    }

    public void setIdFattura(Integer idFattura) {
        this.idFattura = idFattura;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Double getImponibile() {
        return imponibile;
    }

    public void setImponibile(Double imponibile) {
        this.imponibile = imponibile;
    }

    public Double getTotale() {
        return totale;
    }

    public void setTotale(Double totale) {
        this.totale = totale;
    }

    public List<AbbonamentoSottoscritto> getAbbonamentiSottoscritti() {
        return abbonamentiSottoscritti;
    }

    public void setAbbonamentiSottoscritti(List<AbbonamentoSottoscritto> abbonamentiSottoscritti) {
        this.abbonamentiSottoscritti = abbonamentiSottoscritti;
    }

    public List<AbbonamentoMagazzinoSottoscritto> getAbbonamentiMagazzinoSottoscritti() {
        return abbonamentiMagazzinoSottoscritti;
    }

    public void setAbbonamentiMagazzinoSottoscritti(List<AbbonamentoMagazzinoSottoscritto> abbonamentiMagazzinoSottoscritti) {
        this.abbonamentiMagazzinoSottoscritti = abbonamentiMagazzinoSottoscritti;
    }

    public List<Spedizione> getSpedizioni() {
        return spedizioni;
    }

    public void setSpedizioni(List<Spedizione> spedizioni) {
        this.spedizioni = spedizioni;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
