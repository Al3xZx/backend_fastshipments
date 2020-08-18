package com.fastshipmentsdev.backend_fastshipments.b_service;

import com.fastshipmentsdev.backend_fastshipments.d_entity.Abbonamento;
import com.fastshipmentsdev.backend_fastshipments.d_entity.AbbonamentoMagazzino;
import com.fastshipmentsdev.backend_fastshipments.d_entity.CartaCredito;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Fattura;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AbbonamentoMagazzinoService {
    public Set<AbbonamentoMagazzino> allAbbonamenti(){return null;}
    public void sottoscriviAbbonamentoMagazzino(int idC, int idA){}
    public Fattura generaFattura(int idAbbonamento, int idCliente){return null;}
    public void pagamentoAbbonamento(int idC, List<Integer> idA, CartaCredito c){}
    public AbbonamentoMagazzino aggiungiAbbonamentoMagazzino(AbbonamentoMagazzino a){return null;}
    public Abbonamento modificaAbbonamentoMagazzino(int idAbbonamento, AbbonamentoMagazzino a){return null;}
    public void elimina(int idAbbonamento){}
}
