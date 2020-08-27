package com.fastshipmentsdev.backend_fastshipments.b_service;

import com.fastshipmentsdev.backend_fastshipments.c_repository.*;
import com.fastshipmentsdev.backend_fastshipments.d_entity.*;
import com.fastshipmentsdev.backend_fastshipments.support.classi.Indirizzo;
import com.fastshipmentsdev.backend_fastshipments.support.classi.IndirizzoHub;
import com.fastshipmentsdev.backend_fastshipments.support.exception.AbbonamentoNonEsistenteException;
import com.fastshipmentsdev.backend_fastshipments.support.exception.ClienteNonEsistenteException;
import com.fastshipmentsdev.backend_fastshipments.support.exception.PagamentoException;
import com.fastshipmentsdev.backend_fastshipments.support.exception.ServizioInZonaNonDisponibileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
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

    @Autowired
    HubRepository hubRepository;


    @Transactional(readOnly = true)
    public List<Abbonamento> allAbbonamenti(int resForPage, int page){
        return abbonamentoRepository.selAll(PageRequest.of(page, resForPage));
    }

    @Transactional(readOnly = false)
    public AbbonamentoSottoscritto sottoscriviAbbonamento(int idC, int idA, CartaCredito cartaCredito)
            throws ClienteNonEsistenteException, AbbonamentoNonEsistenteException, PagamentoException, ServizioInZonaNonDisponibileException {
        Optional<Cliente> oC = clienteRepository.findById(idC);
        if(!oC.isPresent()) throw new ClienteNonEsistenteException();
        Optional<Abbonamento> oA = abbonamentoRepository.findById(idA);
        if(!oA.isPresent()) throw new AbbonamentoNonEsistenteException();
        AbbonamentoSottoscritto aS = new AbbonamentoSottoscritto();

        String regioneCliente = Indirizzo.parse(oC.get().getIndirizzo()).getRegione();
        List<Hub> hubs = hubRepository.selAllHub();
        Hub posibileHubServizio = null;
        for(Hub h : hubs){
            IndirizzoHub ih = IndirizzoHub.parse(h.getIndirizzo());
            if(ih.getRegione().toUpperCase().equals(regioneCliente.toUpperCase()))
                posibileHubServizio = h;
        }
        if(posibileHubServizio == null)
            throw new ServizioInZonaNonDisponibileException();
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

    @Transactional(readOnly = true)
    public List<AbbonamentoSottoscritto> abbonamentiSottoscritti(int idCliente) throws ClienteNonEsistenteException {
        Optional<Cliente> oC = clienteRepository.findById(idCliente);
        if(!oC.isPresent()) throw new ClienteNonEsistenteException();
        Cliente c = oC.get();
        List<AbbonamentoSottoscritto> ret = new LinkedList<>();
        for(AbbonamentoSottoscritto a : c.getAbbonamenti())
            if(a.getDataFine().compareTo(LocalDateTime.now())>0)
                ret.add(a);
        return ret;
    }

}
