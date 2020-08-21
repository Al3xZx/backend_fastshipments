package com.fastshipmentsdev.backend_fastshipments.b_service;

import com.fastshipmentsdev.backend_fastshipments.c_repository.*;
import com.fastshipmentsdev.backend_fastshipments.d_entity.*;
import com.fastshipmentsdev.backend_fastshipments.support.classi.Indirizzo;
import com.fastshipmentsdev.backend_fastshipments.support.classi.MansioneDipendente;
import com.fastshipmentsdev.backend_fastshipments.support.classi.StatoSpedizione;
import com.fastshipmentsdev.backend_fastshipments.support.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Transactional(readOnly = false)
    public Spedizione aggiungi(int idCliente, Spedizione s, CartaCredito cartaCredito)
            throws ClienteNonEsistenteException, PagamentoException {

        Optional<Cliente> c = clienteRepository.findById(s.getMittente().getIdCliente());
        if(!c.isPresent()) throw new ClienteNonEsistenteException();
        Cliente cliente = c.get();
        s.setMittente(cliente);
        s.setPesoTassabile(s.getVolume()*300);
        Fattura f = generaFattura(cliente, cartaCredito, s);
        s.setFattura(f);

        Hub hubDestinazione = hubRepository.findByRegioneContaining(Indirizzo.parse(s.getIndirizzoDestinatario()).getRegione());
        Hub hubPartenza = hubRepository.findByRegioneContaining(Indirizzo.parse(cliente.getIndirizzo()).getRegione());
        s.setHubDestinazione(hubDestinazione);
        s.setHubDestinazione(hubPartenza);
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

    @Transactional(readOnly = false)
    public Spedizione spedizioneDaAbbonamento(Integer idAbbonamento, Integer idC, Spedizione s)
            throws ClienteNonEsistenteException, AbbonamentoNonEsistenteException,
            AbbonamentoNonAssociatoException, SpedizioniTerminateException{

        Optional<Cliente> oC = clienteRepository.findById(idC);
        if(!oC.isPresent()) throw new ClienteNonEsistenteException();
        Cliente cliente = oC.get();

        Optional<AbbonamentoSottoscritto> oA = abbonamentoSottoscrittoRepository.findById(idAbbonamento);
        if(!oA.isPresent()) throw new AbbonamentoNonEsistenteException();
        AbbonamentoSottoscritto AS = oA.get();

        if(!cliente.getAbbonamenti().contains(AS))
            throw new AbbonamentoNonAssociatoException();

        if(AS.getAbbonamento().getNumeroSpedizioni().compareTo(AS.getSpedizioniEffettuate()+1)<0)
            throw new SpedizioniTerminateException();

        AS.setSpedizioniEffettuate(AS.getSpedizioniEffettuate()+1);//incremento le spedizioni effettuate

        s.setMittente(cliente);
        s.setPesoTassabile(s.getVolume()*300);
        s.setStato(StatoSpedizione.DA_RITIRARE);

        Hub hubDestinazione = hubRepository.findByRegioneContaining(Indirizzo.parse(s.getIndirizzoDestinatario()).getRegione());
        Hub hubPartenza = hubRepository.findByRegioneContaining(Indirizzo.parse(cliente.getIndirizzo()).getRegione());
        s.setHubDestinazione(hubDestinazione);
        s.setHubDestinazione(hubPartenza);
        //assegno il ritiro ai trasportatori urbani
        for(Dipendente d : hubPartenza.getDipendenti()){
            if(d.getMansione().equals(MansioneDipendente.TRASPORTATORE_URBANO))
                d.getSpedizioniDaRitirare().add(s);
        }

        return spedizioneRepository.save(s);
    }

    @Transactional(readOnly = false)
    public void spedizioneDaMagazzino(Integer idAbbonamentoMagazzino, Integer idC, List<Integer> idMerce, Indirizzo indirizzoDiDestinazione)
            throws AbbonamentoNonEsistenteException, ClienteNonEsistenteException, MerceNonEsistenteException, MerceNonAssociataException, AbbonamentoNonAssociatoException {
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
        for(Integer i : idMerce){
            Optional<Merce> tmp = merceRepository.findById(i);
            if(!tmp.isPresent() )
                throw new MerceNonEsistenteException();
            if(!(cliente.get().getMerceProprietario().contains(merceRepository.findById(i))))
                throw new MerceNonAssociataException();
            s.getMerci().add(tmp.get());
            tmp.get().setSpedizione(s);
            s.setPesoTassabile(s.getPesoTassabile()+(tmp.get().getVolume()*300));
        }
        s.setStato(StatoSpedizione.IN_LAVORAZIONE);
        s.setMittente(cliente.get());

        Hub hubDestinazione = hubRepository.findByRegioneContaining(indirizzoDiDestinazione.getRegione());
        Hub hubPartenza = abbonamento.get().getHub();
        s.setHubDestinazione(hubDestinazione);
        s.setHubDestinazione(hubPartenza);

        spedizioneRepository.save(s);
    }

    @Transactional(readOnly = false)
    public void posticipaConsegna (Integer idSpedizione, LocalDateTime d) throws SpedizioneNonEsistenteException {
        Optional<Spedizione> spedizione = spedizioneRepository.findById(idSpedizione);
        if(!spedizione.isPresent())
            throw new SpedizioneNonEsistenteException();
        spedizione.get().setDataArrivo(d);
    }

    @Transactional(readOnly = true)
    public List<Spedizione> spedizioniEffettuate(int idCliente) throws ClienteNonEsistenteException {
        Optional<Cliente> oC = clienteRepository.findById(idCliente);
        if(!oC.isPresent()) throw new ClienteNonEsistenteException();
        return spedizioneRepository.findAllByMittente(oC.get());
    }

}
