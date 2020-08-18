package com.fastshipmentsdev.backend_fastshipments.b_service;

import com.fastshipmentsdev.backend_fastshipments.d_entity.CartaCredito;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Fattura;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Spedizione;

import java.util.Date;
import java.util.List;

public class SpedizioneService {
    public Spedizione aggiungi(Spedizione s ){
        return null;
    }

    public Spedizione ricerca(Integer idSpedizione){
        return null;
    }

    public void pagamentoSpedizione(Integer idSpedizione, Integer idC, CartaCredito c){

    }

    public Fattura generaFattura(Integer idspedizione, Integer idCliente){
        return null;
    }

    public void spedizioneDaAbbonamento(Integer idAbbonamento, Integer idC, Spedizione s){

    }

    public void spedizioneDaMagazzino(Integer idAbbonamentoMagazzino, Integer idC, List<Integer> idMerce){

    }

    public void posticipaConsegna (Integer idSpedizione, Date d){

    }

}
