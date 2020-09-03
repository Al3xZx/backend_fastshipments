package com.fastshipmentsdev.backend_fastshipments.b_service;

import com.fastshipmentsdev.backend_fastshipments.c_repository.*;
import com.fastshipmentsdev.backend_fastshipments.d_entity.*;
import com.fastshipmentsdev.backend_fastshipments.support.classi.Indirizzo;
import com.fastshipmentsdev.backend_fastshipments.support.classi.StatoMerce;
import com.fastshipmentsdev.backend_fastshipments.support.exception.*;
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

    @Autowired
    HubRepository hubRepository;

    @Autowired
    MerceRepository merceRepository;


    @Transactional(readOnly = true)
    public List<AbbonamentoMagazzino> allAbbonamentiMagazzino(int resForPage, int page){
        return abbonamentoMagazzinoRepository.selAll(PageRequest.of(page, resForPage));
    }

    @Transactional(readOnly = true)
    public List<AbbonamentoMagazzino> allAbbonamentiMagazzino(){
        return abbonamentoMagazzinoRepository.findAll();
    }

    @Transactional(readOnly = false)
    public AbbonamentoMagazzinoSottoscritto sottoscriviAbbonamento(int idC, int idA, int idHub, CartaCredito cartaCredito)
            throws ClienteNonEsistenteException, AbbonamentoNonEsistenteException, PagamentoException, HubNonEsistenteException, SpazioNonDisponibileException {
        Optional<Cliente> oC = clienteRepository.findById(idC);
        if(!oC.isPresent()) throw new ClienteNonEsistenteException();

        Optional<AbbonamentoMagazzino> oA = abbonamentoMagazzinoRepository.findById(idA);
        if(!oA.isPresent()) throw new AbbonamentoNonEsistenteException();
        AbbonamentoMagazzino abbonamenScelto = oA.get();
        Optional<Hub> oH = hubRepository.findById(idHub);
        if(!oH.isPresent()) throw new HubNonEsistenteException();
        Hub hubScelto = oH.get();
        //verifica spazio nell'hub
        if(abbonamenScelto.getVolumeDisponibile().compareTo(hubScelto.getVolumeDisponibile())>0)
            throw new SpazioNonDisponibileException();
        AbbonamentoMagazzinoSottoscritto aS = new AbbonamentoMagazzinoSottoscritto();
        aS.setHub(hubScelto);
        aS.setCliente(oC.get());
        aS.setAbbonamentoMagazzino(abbonamenScelto);
        aS.setDataFine(aS.getDataInizio().plusDays(abbonamenScelto.getDurata()));
        Fattura f = generaFattura(oC.get(), aS, cartaCredito);
        hubScelto.setVolumeDisponibile(hubScelto.getVolumeDisponibile() - abbonamenScelto.getVolumeDisponibile());
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
        if(!oCC.isPresent()) throw new PagamentoException();
        CartaCredito cartaCreditoP = oCC.get();
        if(!cartaCreditoP.equals(cartaCredito)) throw new PagamentoException();
        if(cartaCreditoP.getSaldoDisponibile().compareTo(fattura.getTotale())<0)throw new PagamentoException();
        cartaCreditoP.setSaldoDisponibile(cartaCreditoP.getSaldoDisponibile()-totale);
    }

    @Transactional(readOnly = true)
    public List<AbbonamentoMagazzinoSottoscritto> abbonamentiMagazzinoSottoscritti(int idCliente) throws ClienteNonEsistenteException {
        Optional<Cliente> oC = clienteRepository.findById(idCliente);
        if(!oC.isPresent()) throw new ClienteNonEsistenteException();
        Cliente c = oC.get();
        List<AbbonamentoMagazzinoSottoscritto> ret = new LinkedList<>();
        for(AbbonamentoMagazzinoSottoscritto a : c.getAbbonamentiMagazzino())
            if(a.getDataFine().compareTo(LocalDateTime.now())>0)
                ret.add(a);
        return ret;
    }

    @Transactional(readOnly = false)
    public List<Merce> richiestaRitiroMerce(int idCliente, int idAbbonamentoMagazzinoSottoscritto, Merce merce, int numeroMerci)
            throws ClienteNonEsistenteException, AbbonamentoNonEsistenteException, SpazioNonDisponibileException, AbbonamentoNonAssociatoException, AbbonamentoScadutoException {
        Optional<Cliente> oC = clienteRepository.findById(idCliente);
        if(!oC.isPresent()) throw new ClienteNonEsistenteException();
        Cliente cliente = oC.get();
        Optional<AbbonamentoMagazzinoSottoscritto> oAMS = abbonamentoMagazzinoSottoscrittoRepository.findById(idAbbonamentoMagazzinoSottoscritto);
        if(!oAMS.isPresent()) throw new AbbonamentoNonEsistenteException();
        AbbonamentoMagazzinoSottoscritto AMS = oAMS.get();

        if(AMS.getDataFine().compareTo(LocalDateTime.now())<0)
            throw new AbbonamentoScadutoException();

        if(!AMS.getCliente().getIdCliente().equals(idCliente))
            throw new AbbonamentoNonAssociatoException();
        double volumeMerce = merce.getVolume();
        if(AMS.getAbbonamentoMagazzino().getVolumeDisponibile().compareTo(AMS.getVolumeUtilizzato()+(volumeMerce*numeroMerci))<0)
            throw new SpazioNonDisponibileException();

        AMS.setVolumeUtilizzato(AMS.getVolumeUtilizzato()+(volumeMerce*numeroMerci));
        merce.setProprietario(cliente);
        merce.setStato(StatoMerce.DA_RITIRARE);
        merce.setAbbonamentoMagazzinoSottoscritto(AMS);
        List<Merce> ret = new LinkedList<>();
        for (int i = 0; i < numeroMerci; i++) {
            Merce mTMP = merceRepository.save(new Merce(merce));
            cliente.getMerceProprietario().add(mTMP);
            ret.add(mTMP);
        }
        return ret;
    }

    @Transactional(readOnly = true)
    public List<Merce> ricercaMerceDescr(int idCliente, String descrizione) throws ClienteNonEsistenteException{
        Optional<Cliente> oC = clienteRepository.findById(idCliente);
        if(!oC.isPresent()) throw new ClienteNonEsistenteException();
        List<Merce> ret = new LinkedList<>();
        for (Merce m: oC.get().getMerceProprietario()) {
            if(m.getDescrizione().contains(descrizione))
                ret.add(m);
        }
        return  ret;
    }

    @Transactional(readOnly = true)
    public List<Merce> merceInMagazzino(int idCliente, int idAbbonamentoMagSot) throws ClienteNonEsistenteException, AbbonamentoNonEsistenteException, AbbonamentoNonAssociatoException {
        Optional<Cliente> oC = clienteRepository.findById(idCliente);
        if(!oC.isPresent()) throw new ClienteNonEsistenteException();
        Optional<AbbonamentoMagazzinoSottoscritto> oAMS = abbonamentoMagazzinoSottoscrittoRepository.findById(idAbbonamentoMagSot);
        if(!oAMS.isPresent()) throw new AbbonamentoNonEsistenteException();
        AbbonamentoMagazzinoSottoscritto AMS = oAMS.get();
        if(!AMS.getCliente().getIdCliente().equals(idCliente))
            throw new AbbonamentoNonAssociatoException();
        Hub hub = AMS.getHub();
        List<Merce> ret = new LinkedList<>();
        for (Merce m: oC.get().getMerceProprietario()) {
            if(m.getScaffale() != null && m.getAbbonamentoMagazzinoSottoscritto() != null
                    && m.getAbbonamentoMagazzinoSottoscritto().getIdAbbonamento().equals(AMS.getIdAbbonamento())
                    && m.getStato().equals(StatoMerce.STOCCATA) )
                ret.add(m);

        }
        return ret;
    }

//    @Transactional(readOnly = true)
//    public List<Merce> merceInMagazzino(int idCliente)
//            throws ClienteNonEsistenteException {
//        Optional<Cliente> oC = clienteRepository.findById(idCliente);
//        if(!oC.isPresent()) throw new ClienteNonEsistenteException();
//        List<Merce> ret = new LinkedList<>();
//        for (Merce m: oC.get().getMerceProprietario()) {
//            if(/*!ret.contains(m) &&*/ m.getStato().equals(StatoMerce.STOCCATA)){
////                m.setQta(merceRepository.contaMerceStoccata(m.getDescrizione()));
//                ret.add(m);
//            }
//
//        }
//        return ret;
//    }

    @Transactional(readOnly = true)
    public List<Merce> allMerce(int idCliente)
            throws ClienteNonEsistenteException {
        Optional<Cliente> oC = clienteRepository.findById(idCliente);
        if(!oC.isPresent()) throw new ClienteNonEsistenteException();
        List<Merce> ret = new LinkedList<>();
        for (Merce m: oC.get().getMerceProprietario()) {
            if(!ret.contains(m)){
                m.setQta(merceRepository.contaMerce(m.getDescrizione()));
                ret.add(m);
            }
        }
        return ret;
    }


    @Transactional(readOnly = true)
    public List<Hub> hubDisponibili(){
        return hubRepository.selAllHub();
    }
}
