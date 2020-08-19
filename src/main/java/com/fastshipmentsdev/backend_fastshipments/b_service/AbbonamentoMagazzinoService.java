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
public class AbbonamentoMagazzinoService {

    @Autowired
    AbbonamentoMagazzinoRepository abbonamentoMagazzinoRepository;

    @Autowired
    AbbonamentoMagazzinoSottoscrittoRepository abbonamentoMagazzinoSottoscrittoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    FatturaRepository fatturaRepository;

    @Autowired
    CartaCreditoRepository cartaCreditoRepository;


    @Transactional(readOnly = true)
    public List<Abbonamento> allAbbonamentiMagazzino(int resForPage, int page){
        return abbonamentoMagazzinoRepository.selAll(PageRequest.of(page, resForPage));
    }

    @Transactional(readOnly = false)
    public AbbonamentoMagazzinoSottoscritto sottoscriviAbbonamento(int idC, int idA, CartaCredito cartaCredito)
            throws ClienteNonEsistenteException, AbbonamentoNonEsistenteException, PagamentoException {
        Optional<Cliente> oC = clienteRepository.findById(idC);
        if(!oC.isPresent()) throw new ClienteNonEsistenteException();
        Optional<AbbonamentoMagazzino> oA = abbonamentoMagazzinoRepository.findById(idA);
        if(!oA.isPresent()) throw new AbbonamentoNonEsistenteException();
        AbbonamentoMagazzinoSottoscritto aS = new AbbonamentoMagazzinoSottoscritto();
        aS.setCliente(oC.get());
        aS.setAbbonamentoMagazzino(oA.get());
        aS.setDataFine(aS.getDataInizio().plusDays(oA.get().getDurata()));
        Fattura f = generaFattura(oC.get(), aS, cartaCredito);
        aS.setFattura(f);
        return abbonamentoMagazzinoSottoscrittoRepository.save(aS);
    }

    private Fattura generaFattura(Cliente c, AbbonamentoMagazzinoSottoscritto a, CartaCredito cartaCredito) throws PagamentoException {
        Fattura f = new Fattura();
        f.setCliente(c);
        f.getAbbonamentiMagazzinoSottoscritti().add(a);
        double imponibile = a.getAbbonamentoMagazzino().getCosto();
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

    public Set<AbbonamentoMagazzinoSottoscritto> abbonamentiMagazzinoSottoscritti(int idCliente) throws ClienteNonEsistenteException {
        Optional<Cliente> oC = clienteRepository.findById(idCliente);
        if(!oC.isPresent()) throw new ClienteNonEsistenteException();
        return oC.get().getAbbonamentiMagazzino();
    }
}
