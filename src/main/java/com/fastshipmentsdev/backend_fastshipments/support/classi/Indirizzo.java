package com.fastshipmentsdev.backend_fastshipments.support.classi;

import java.util.StringTokenizer;

public class Indirizzo {

    private String regione;
    private String citta;
    private String provincia;
    private String nome;
    private String cognome;
    private String via;
    private String civico;

    public Indirizzo() {
    }

    public Indirizzo(String regione,
                     String citta,
                     String provincia,
                     String nome, String cognome, String via, String civico) {
        this.regione = regione;
        this.citta = citta;
        this.provincia = provincia;
        this.nome = nome;
        this.cognome = cognome;
        this.via = via;
        this.civico = civico;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
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

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getCivico() {
        return civico;
    }

    public void setCivico(String civico) {
        this.civico = civico;
    }

    @Override
    public String toString() {
        return  "regione: " + regione +
                " citta: " + citta  +
                " provincia: " + provincia +
                " nome: " + nome +
                " cognome: " + cognome +
                " via: " + via +
                " civico: " + civico;
    }

    /**DEVE RICEVERE LA STRINGA FORMATTATA CORRETTAMENTE COME IL ToString()*/
    public static Indirizzo parse(String indirizzo){
        Indirizzo ret = new Indirizzo();
        StringTokenizer st = new StringTokenizer(indirizzo, " ");

        st.nextToken();
        ret.setRegione(st.nextToken());
        st.nextToken();
        ret.setCitta(st.nextToken());
        st.nextToken();
        ret.setProvincia(st.nextToken());
        st.nextToken();
        ret.setNome(st.nextToken());
        st.nextToken();
        ret.setCognome(st.nextToken());
        st.nextToken();
        ret.setVia(st.nextToken());
        st.nextToken();
        ret.setCivico(st.nextToken());

        return ret;
    }

//    public static void main(String[] args) {
//        Indirizzo i = new Indirizzo("calabria","mangone","cs", "Paolo", "Cundari", "test", "25");
//        System.out.println(i);
//        Indirizzo i2 = Indirizzo.parse(i.toString());
//        System.out.println("nuovo indirizzo");
//        System.out.println(i);
//    }

}
