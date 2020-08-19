package com.fastshipmentsdev.backend_fastshipments.d_entity;

import javax.persistence.*;

@Entity
public class CartaCredito {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String numero;

    @Column(nullable = false)
    private String nomeIntestatario;

    @Column(nullable = false)
    private String cognomeIntestatario;

    @Column(nullable = false)
    private String cvv;

    @Column(nullable = false)
    private Double saldoDisponibile;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNomeIntestatario() {
        return nomeIntestatario;
    }

    public void setNomeIntestatario(String nomeIntestatario) {
        this.nomeIntestatario = nomeIntestatario;
    }

    public String getCognomeIntestatario() {
        return cognomeIntestatario;
    }

    public void setCognomeIntestatario(String cognomeIntestatario) {
        this.cognomeIntestatario = cognomeIntestatario;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Double getSaldoDisponibile() {
        return saldoDisponibile;
    }

    public void setSaldoDisponibile(Double saldoDisponibile) {
        this.saldoDisponibile = saldoDisponibile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartaCredito that = (CartaCredito) o;

        if (numero != null ? !numero.equals(that.numero) : that.numero != null) return false;
        if (nomeIntestatario != null ? !nomeIntestatario.equals(that.nomeIntestatario) : that.nomeIntestatario != null)
            return false;
        if (cognomeIntestatario != null ? !cognomeIntestatario.equals(that.cognomeIntestatario) : that.cognomeIntestatario != null)
            return false;
        return cvv != null ? cvv.equals(that.cvv) : that.cvv == null;
    }

    @Override
    public int hashCode() {
        int result = numero != null ? numero.hashCode() : 0;
        result = 31 * result + (nomeIntestatario != null ? nomeIntestatario.hashCode() : 0);
        result = 31 * result + (cognomeIntestatario != null ? cognomeIntestatario.hashCode() : 0);
        result = 31 * result + (cvv != null ? cvv.hashCode() : 0);
        return result;
    }
}
