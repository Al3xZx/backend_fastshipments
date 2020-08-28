package com.fastshipmentsdev.backend_fastshipments.support.classi;

import java.util.StringTokenizer;

public class IndirizzoHub {


    private String regione;
    private String citta;
    private String provincia;
    private String via;
    private String civico;

    public IndirizzoHub() {
    }

    public IndirizzoHub(String regione, String citta, String provincia, String via, String civico) {
        this.regione = regione;
        this.citta = citta;
        this.provincia = provincia;
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
                " via: " + via +
                " civico: " + civico;
    }

    /**DEVE RICEVERE LA STRINGA FORMATTATA CORRETTAMENTE COME IL ToString()*/
    public static IndirizzoHub parse(String indirizzo){
        /*IndirizzoHub ret = new IndirizzoHub();
        StringTokenizer st = new StringTokenizer(indirizzo, " ");

        st.nextToken();
        ret.setRegione(st.nextToken());
        st.nextToken();
        ret.setCitta(st.nextToken());
        st.nextToken();
        ret.setProvincia(st.nextToken());
        st.nextToken();
        ret.setVia(st.nextToken());
        st.nextToken();
        ret.setCivico(st.nextToken());*/

        String[] arSplit = indirizzo.split("\\s?[a-zA-Z]+:\\s");
        IndirizzoHub ret = new IndirizzoHub(arSplit[1],arSplit[2],arSplit[3],arSplit[4],arSplit[5]);
        return ret;
    }

    /*public static void main(String[] args) {
        /*
('1', 'Cosenza', 'Calabria', '150', '150');
('2', 'Trieste', 'FriuliVeneziaGiulia ', '150', '150');
('3', 'Milano', 'Lombardia', '150', '150');
('4', 'Venezia', 'Veneto', '150', '150');
('5', 'Taranto', 'Puglia', '150', '150');
('6', 'Roma', 'Lazio', '150', '150');
('7', 'Napoli', 'Campania', '150', '150');
('8', 'Macerata', 'Marche', '150', '150');
('9', 'Firenze', 'Toscana', '150', '150');
 ('10', 'Palermo', 'Sicilia', '150', '150');
        IndirizzoHub[] hubs = new IndirizzoHub[10];
        hubs[0]= new IndirizzoHub("Calabria","Cosenza","CS","test", "25");
        hubs[1]= new IndirizzoHub("Friuli Venezia Giulia","Trieste","TS","test", "25");
        hubs[2]= new IndirizzoHub("Lombardia","Milano","MI","test", "25");
        hubs[3]= new IndirizzoHub("Veneto","Venezia","VE","test", "25");
        hubs[4]= new IndirizzoHub("Puglia","Taranto","TA","test", "25");
        hubs[5]= new IndirizzoHub("Lazio","Roma","RM","test", "25");
        hubs[6]= new IndirizzoHub("Campania","Napoli","NA","test", "25");
        hubs[7]= new IndirizzoHub("Marche","Macerata","MC","test", "25");
        hubs[8]= new IndirizzoHub("Toscana","Firenze","FI","test", "25");
        hubs[9]= new IndirizzoHub("Sicilia","Palermo","PA","test dei test", "25");
        for(IndirizzoHub ih : hubs){
            System.out.println(ih);
        }

        IndirizzoHub i = IndirizzoHub.parse(hubs[9].toString());
        System.out.println(i);
    }*/
}
