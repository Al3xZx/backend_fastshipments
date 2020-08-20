package com.fastshipmentsdev.backend_fastshipments.b_service;

import com.fastshipmentsdev.backend_fastshipments.c_repository.*;
import com.fastshipmentsdev.backend_fastshipments.d_entity.*;
import com.fastshipmentsdev.backend_fastshipments.support.exception.AbbonamentoNonEsistenteException;
import com.fastshipmentsdev.backend_fastshipments.support.exception.ClienteNonEsistenteException;
import com.fastshipmentsdev.backend_fastshipments.support.exception.PagamentoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AbbonamentoService {

    @Autowired
    AbbonamentoRepository abbonamentoRepository;

    @Autowired
    AbbonamentoSottoscrittoRepository abbonamentoSottoscrittoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    FatturaRepository fatturaRepository;

    @Autowired
    CartaCreditoRepository cartaCreditoRepository;


    @Transactional(readOnly = true)
    public List<Abbonamento> allAbbonamenti(int resForPage, int page){
        return abbonamentoRepository.selAll(PageRequest.of(page, resForPage));
    }

    @Transactional(readOnly = false)
    public AbbonamentoSottoscritto sottoscriviAbbonamento(int idC, int idA, CartaCredito cartaCredito)
            throws ClienteNonEsistenteException, AbbonamentoNonEsistenteException, PagamentoException {
        Optional<Cliente> oC = clienteRepository.findById(idC);
        if(!oC.isPresent()) throw new ClienteNonEsistenteException();
        Optional<Abbonamento> oA = abbonamentoRepository.findById(idA);
        if(!oA.isPresent()) throw new AbbonamentoNonEsistenteException();
        AbbonamentoSottoscritto aS = new AbbonamentoSottoscritto();
        aS.setCliente(oC.get());
        aS.setAbbonamento(oA.get());
        aS.setDataFine(aS.getDataInizio().plusDays(oA.get().getDurata()));
        Fattura f = generaFattura(oC.get(), aS, cartaCredito);
        aS.setFattura(f);
        return abbonamentoSottoscrittoRepository.save(aS);
    }

    private Fattura generaFattura(Cliente c, AbbonamentoSottoscritto a, CartaCredito cartaCredito) throws PagamentoException {
        Fattura f = new Fattura();
        f.setCliente(c);
        f.getAbbonamentiSottoscritti().add(a);
        double imponibile = a.getAbbonamento().getCosto();
        f.setImponibile(imponibile);
        double imposta = (imponibile*22)/100;
        f.setTotale(imponibile+imposta);
        pagamentoAbbonamento(f, cartaCredito);
        return fatturaRepository.save(f);
    }

    private void pagamentoAbbonamento(Fattura fattura, CartaCredito cartaCredito) throws PagamentoException {
        double totale = fattura.getTotale();
        Optional<CartaCredito> oCC = cartaCreditoRepository.findById(cartaCredito.getNumero());
        CartaCredito cartaCreditoP = oCC.get();
        if(!oCC.isPresent()) throw new PagamentoException();
        if(!cartaCreditoP.equals(cartaCredito)) throw new PagamentoException();
        if(cartaCreditoP.getSaldoDisponibile().compareTo(fattura.getTotale())<0)throw new PagamentoException();
        cartaCreditoP.setSaldoDisponibile(cartaCreditoP.getSaldoDisponibile()-totale);
    }

    public Set<AbbonamentoSottoscritto> abbonamentiSottoscritti(int idCliente) throws ClienteNonEsistenteException {
        Optional<Cliente> oC = clienteRepository.findById(idCliente);
        if(!oC.isPresent()) throw new ClienteNonEsistenteException();
        return oC.get().getAbbonamenti();
    }

//    @Transactional(readOnly = false)
//    public Abbonamento aggiungiAbbonamento (Abbonamento a) {
//        Optional<Dipendente> direttoreVendite = dipendenteRepository.findById(a.getDirettoreVendite().getIdDipendente());
//        if(!oA.isPresent()) throw new DipendenteNonEsistenteException();
//        return abbonamentoRepository.save(a);
//    }
//
//    @Transactional(readOnly = false)
//    public Abbonamento modifica(int idAbbonamento, Abbonamento a) throws AbbonamentoNonEsistenteException {
//        Optional<Abbonamento> oA = abbonamentoRepository.findById(idAbbonamento);
//        if(!oA.isPresent()) throw new AbbonamentoNonEsistenteException();
//        Abbonamento abbonamentoP = oA.get();
//        abbonamentoP.setCosto(a.getCosto());
//        abbonamentoP.setDescrizione(a.getDescrizione());
//        abbonamentoP.setDirettoreVendite(a.getDirettoreVendite());
//        abbonamentoP.setDurata(a.getDurata());
//        abbonamentoP.setNumeroSpedizioni(a.getNumeroSpedizioni());
//        return abbonamentoP;
//    }
//
//    @Transactional(readOnly = false)
//    public void elimina (int idAbbonamento) throws AbbonamentoNonEsistenteException {
//        Optional<Abbonamento> oA = abbonamentoRepository.findById(idAbbonamento);
//        if(!oA.isPresent()) throw new AbbonamentoNonEsistenteException();
//        abbonamentoRepository.deleteById(idAbbonamento);
//    }

}
