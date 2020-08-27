package com.fastshipmentsdev.backend_fastshipments.b_service;

import com.fastshipmentsdev.backend_fastshipments.c_repository.*;
import com.fastshipmentsdev.backend_fastshipments.d_entity.*;
import com.fastshipmentsdev.backend_fastshipments.support.classi.*;
import com.fastshipmentsdev.backend_fastshipments.support.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class SpedizioneService {

    @Autowired
    SpedizioneRepository spedizioneRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    AbbonamentoSottoscrittoRepository abbonamentoSottoscrittoRepository;

    @Autowired
    AbbonamentoMagazzinoSottoscrittoRepository abbonamentoMagazzinoSottoscrittoRepository;

    @Autowired
    MerceRepository merceRepository;

    @Autowired
    FatturaRepository fatturaRepository;

    @Autowired
    CartaCreditoRepository cartaCreditoRepository;

    @Autowired
    HubRepository hubRepository;

    @Transactional(readOnly = false) //todo test dopo modifica hub
    public Spedizione aggiungi(int idCliente, Spedizione s, CartaCredito cartaCredito)
            throws ClienteNonEsistenteException, PagamentoException, ServizioInZonaNonDisponibileException {
        Optional<Cliente> c = clienteRepository.findById(idCliente);
        if(!c.isPresent()) throw new ClienteNonEsistenteException();
        Cliente cliente = c.get();
        s.setMittente(cliente);
        s.setPesoTassabile(s.getVolume()*300);
        s.getIndirizzoDestinazione().setProvincia(s.getIndirizzoDestinazione().getProvincia().toUpperCase());

        //Fattura f = generaFattura(cliente, cartaCredito, s);
        //s.setFattura(f);

        String regTMP = s.getIndirizzoDestinazione().getRegione();
        String regioneDest = Character.toString(regTMP.charAt(0)).toUpperCase()+regTMP.substring(1).toLowerCase();
        s.getIndirizzoDestinazione().setRegione(regioneDest);

        List<Hub> hubs = hubRepository.selAllHub();
        Hub hubDestinazione = null;
        for(Hub h : hubs){
            IndirizzoHub ih = IndirizzoHub.parse(h.getIndirizzo());
            if(ih.getRegione().toUpperCase().equals(regioneDest.toUpperCase()))
                hubDestinazione = h;
        }
        Hub hubPartenza = null;
        for(Hub h : hubs){
            Indirizzo iC = Indirizzo.parse(cliente.getIndirizzo());
            IndirizzoHub ih = IndirizzoHub.parse(h.getIndirizzo());
            if(ih.getRegione().toUpperCase().equals(iC.getRegione().toUpperCase()))
                hubPartenza = h;
        }
        if(hubPartenza == null || hubDestinazione == null) throw new ServizioInZonaNonDisponibileException();

        Fattura f = generaFattura(cliente, cartaCredito, s);
        s.setFattura(f);

//        Hub hubDestinazione = hubRepository.findByRegioneContaining(regioneDest);
//        Hub hubPartenza = hubRepository.findByRegioneContaining(Indirizzo.parse(cliente.getIndirizzo()).getRegione());

        s.setHubDestinazione(hubDestinazione);
        s.setHubPartenza(hubPartenza);
        s.setIndirizzoDestinatario(s.getIndirizzoDestinazione().toString());
        //assegno il ritiro ai trasportatori urbani
        for(Dipendente d : hubPartenza.getDipendenti()){
            if(d.getMansione().equals(MansioneDipendente.TRASPORTATORE_URBANO) &&
                    d.getAreaDiCompetenza().getProvincia().equals(Indirizzo.parse(cliente.getIndirizzo()).getProvincia()))
                d.getSpedizioniDaRitirare().add(s);
        }
        s.setStato(StatoSpedizione.DA_RITIRARE);
        return spedizioneRepository.save(s);
    }

    private Fattura generaFattura(Cliente cliente, CartaCredito cartaCredito, Spedizione s) throws PagamentoException {
        Fattura f = new Fattura();
        f.setCliente(cliente);
        f.getSpedizioni().add(s);
        double imponibile = s.getPesoTassabile()/100;//10€ al m^3 (peso tassabile)
        f.setImponibile(imponibile);
        double imposta = (imponibile*22)/100;
        f.setTotale(imponibile+imposta);
        pagamentoFattura(f, cartaCredito);
        return fatturaRepository.save(f);
    }

    private void pagamentoFattura(Fattura fattura, CartaCredito cartaCredito) throws PagamentoException {
        double totale = fattura.getTotale();
        Optional<CartaCredito> oCC = cartaCreditoRepository.findById(cartaCredito.getNumero());
        CartaCredito cartaCreditoP = oCC.get();
        if(!oCC.isPresent()) throw new PagamentoException();
        if(!cartaCreditoP.equals(cartaCredito)) throw new PagamentoException();
        if(cartaCreditoP.getSaldoDisponibile().compareTo(fattura.getTotale())<0)throw new PagamentoException();
        cartaCreditoP.setSaldoDisponibile(cartaCreditoP.getSaldoDisponibile()-totale);
    }

    @Transactional(readOnly = true)
    public Spedizione ricerca(Integer idSpedizione) throws SpedizioneNonEsistenteException {

        Optional<Spedizione> s = spedizioneRepository.findById(idSpedizione);
        if(!s.isPresent()) throw new SpedizioneNonEsistenteException();
        return s.get();
    }

    @Transactional(readOnly = false)  //todo test dopo modifica hub
    public Spedizione spedizioneDaAbbonamento(Integer idAbbonamentoSottoscritto, Integer idC, Spedizione s)
            throws ClienteNonEsistenteException, AbbonamentoNonEsistenteException,
            AbbonamentoNonAssociatoException, SpedizioniTerminateException, AbbonamentoScadutoException, ServizioInZonaNonDisponibileException {

        Optional<Cliente> oC = clienteRepository.findById(idC);
        if(!oC.isPresent()) throw new ClienteNonEsistenteException();
        Cliente cliente = oC.get();

        Optional<AbbonamentoSottoscritto> oA = abbonamentoSottoscrittoRepository.findById(idAbbonamentoSottoscritto);
        if(!oA.isPresent()) throw new AbbonamentoNonEsistenteException();
        AbbonamentoSottoscritto AS = oA.get();

        if(!AS.getCliente().getIdCliente().equals(idC))
            throw new AbbonamentoNonAssociatoException();

        if(AS.getAbbonamento().getNumeroSpedizioni().compareTo(AS.getSpedizioniEffettuate()+1)<0)
            throw new SpedizioniTerminateException();

        if(AS.getDataFine().compareTo(LocalDateTime.now())<0)
            throw new AbbonamentoScadutoException();

        AS.setSpedizioniEffettuate(AS.getSpedizioniEffettuate()+1);//incremento le spedizioni effettuate

        s.setMittente(cliente);
        s.setPesoTassabile(s.getVolume()*300);
        s.setStato(StatoSpedizione.DA_RITIRARE);
        s.getIndirizzoDestinazione().setProvincia(s.getIndirizzoDestinazione().getProvincia().toUpperCase());

        String regTMP = s.getIndirizzoDestinazione().getRegione();
        String regioneDest = Character.toString(regTMP.charAt(0)).toUpperCase()+regTMP.substring(1).toLowerCase();
        s.getIndirizzoDestinazione().setRegione(regioneDest);

        List<Hub> hubs = hubRepository.selAllHub();
        Hub hubDestinazione = null;
        for(Hub h : hubs){
            IndirizzoHub ih = IndirizzoHub.parse(h.getIndirizzo());
            if(ih.getRegione().toUpperCase().equals(regioneDest.toUpperCase()))
                hubDestinazione = h;
        }
        Hub hubPartenza = null;
        for(Hub h : hubs){
            Indirizzo iC = Indirizzo.parse(cliente.getIndirizzo());
            IndirizzoHub ih = IndirizzoHub.parse(h.getIndirizzo());
            if(iC.getRegione().toUpperCase().equals(ih.getRegione().toUpperCase()))
                hubPartenza = h;
        }

        if(hubPartenza == null || hubDestinazione == null) throw new ServizioInZonaNonDisponibileException();

//        Hub hubDestinazione = hubRepository.findByRegioneContaining(regioneDest);
//        Hub hubPartenza = hubRepository.findByRegioneContaining(Indirizzo.parse(cliente.getIndirizzo()).getRegione());
        s.setHubDestinazione(hubDestinazione);
        s.setHubPartenza(hubPartenza);
        s.setIndirizzoDestinatario(s.getIndirizzoDestinazione().toString());
        //assegno il ritiro ai trasportatori urbani
        for(Dipendente d : hubPartenza.getDipendenti()){
            if(d.getMansione().equals(MansioneDipendente.TRASPORTATORE_URBANO)  &&
                    d.getAreaDiCompetenza().getProvincia().equals(Indirizzo.parse(cliente.getIndirizzo()).getProvincia()))
                d.getSpedizioniDaRitirare().add(s);
        }

        return spedizioneRepository.save(s);
    }


    @Transactional(readOnly = false) //todo test dopo modifica hub
    public Spedizione spedizioneDaMagazzino(Integer idAbbonamentoMagazzino, Integer idC, List<Integer> idMerce, Indirizzo indirizzoDiDestinazione)
            throws AbbonamentoNonEsistenteException, ClienteNonEsistenteException, MerceNonEsistenteException, MerceNonAssociataException, AbbonamentoNonAssociatoException, MerceNonStoccataException, MerceNonDisponibileException, ServizioInZonaNonDisponibileException {
        Optional<AbbonamentoMagazzinoSottoscritto> abbonamento = abbonamentoMagazzinoSottoscrittoRepository.findById(idAbbonamentoMagazzino);
        if(!abbonamento.isPresent())
            throw new AbbonamentoNonEsistenteException();
        Optional<Cliente> cliente = clienteRepository.findById(idC);
        if(!cliente.isPresent())
            throw new ClienteNonEsistenteException();

        if(!cliente.get().getAbbonamentiMagazzino().contains(abbonamento.get()))
            throw new AbbonamentoNonAssociatoException();

        Spedizione s = new Spedizione();
        s.setPesoTassabile(0.0);
        s.setVolume(0.0);
        List<Merce> merciTmp = new LinkedList<>();
        for(Integer i : idMerce){
            Optional<Merce> tmp = merceRepository.findById(i);

            if(!tmp.isPresent() )
                throw new MerceNonEsistenteException();
            Merce m = tmp.get();
            if(!m.getStato().equals(StatoMerce.STOCCATA))
                throw new MerceNonStoccataException();
            if(m.getStato().equals(StatoMerce.IN_LAVORAZIONE))
                throw new MerceNonDisponibileException();
            if(!(cliente.get().getMerceProprietario().contains(m)))
                throw new MerceNonAssociataException();
            m.setStato(StatoMerce.IN_LAVORAZIONE);
            s.getMerci().add(m);
            //m.setSpedizione(s);
            merciTmp.add(m);
            s.setPesoTassabile(s.getPesoTassabile()+(tmp.get().getVolume()*300));
            s.setVolume(s.getVolume()+m.getVolume());
        }//for

        s.setIndirizzoDestinazione(indirizzoDiDestinazione);
        s.setStato(StatoSpedizione.IN_LAVORAZIONE);
        s.setMittente(cliente.get());

        s.getIndirizzoDestinazione().setProvincia(s.getIndirizzoDestinazione().getProvincia().toUpperCase());

        String regTMP = s.getIndirizzoDestinazione().getRegione();
        String regioneDest = Character.toString(regTMP.charAt(0)).toUpperCase()+regTMP.substring(1).toLowerCase();
        s.getIndirizzoDestinazione().setRegione(regioneDest);
        s.setIndirizzoDestinatario(s.getIndirizzoDestinazione().toString());

        List<Hub> hubs = hubRepository.selAllHub();
        Hub hubDestinazione = null;
        for(Hub h : hubs){
            IndirizzoHub ih = IndirizzoHub.parse(h.getIndirizzo());
            if(ih.getRegione().toUpperCase().equals(regioneDest.toUpperCase()))
                hubDestinazione = h;
        }

        if(hubDestinazione == null) throw new ServizioInZonaNonDisponibileException();

//        Hub hubDestinazione = hubRepository.findByRegioneContaining(regioneDest);
        Hub hubPartenza = abbonamento.get().getHub();
        s.setHubDestinazione(hubDestinazione);
        s.setHubPartenza(hubPartenza);

        Spedizione ret = spedizioneRepository.save(s);
        for (Merce m : merciTmp)
            m.setSpedizione(ret);
        
        return ret;
    }

    @Transactional(readOnly = false)
    public Spedizione posticipaConsegna (Integer idSpedizione, LocalDateTime d) throws SpedizioneNonEsistenteException, DateException {
        Optional<Spedizione> spedizione = spedizioneRepository.findById(idSpedizione);
        if(!spedizione.isPresent())
            throw new SpedizioneNonEsistenteException();
        Spedizione s = spedizione.get();
        if(d.compareTo(s.getDataArrivo())<0)
            throw new DateException();
        s.setDataArrivo(d);
        return s;
    }

    @Transactional(readOnly = true)
    public List<Spedizione> spedizioniEffettuate(int idCliente) throws ClienteNonEsistenteException {
        Optional<Cliente> oC = clienteRepository.findById(idCliente);
        if(!oC.isPresent()) throw new ClienteNonEsistenteException();
        return spedizioneRepository.findAllByMittente(oC.get());
    }

}
