package com.fastshipmentsdev.backend_fastshipments.b_service;

import com.fastshipmentsdev.backend_fastshipments.d_entity.Abbonamento;
import com.fastshipmentsdev.backend_fastshipments.d_entity.CartaCredito;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Fattura;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AbbonamentoService {

    public Set<Abbonamento> allAbbonamenti(){return null;}

    public void sottoscriviAbbonamento(int idC, int idA) {}

    private Fattura generaFattura(int idAbbonamento, int idCliente){return null;}

    public void pagamentoAbbonamento(int idC, List<Integer> idA, CartaCredito c) {}

    public Abbonamento aggiungiAbbonamento (Abbonamento a) {return null;}

    public Abbonamento modifica(int idAbbonamento, Abbonamento a) {return null;}

    public void elimina (int idAbbonamento){}
}
