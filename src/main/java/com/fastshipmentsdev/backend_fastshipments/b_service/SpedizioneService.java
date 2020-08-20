package com.fastshipmentsdev.backend_fastshipments.b_service;

import com.fastshipmentsdev.backend_fastshipments.c_repository.*;
import com.fastshipmentsdev.backend_fastshipments.d_entity.*;
import com.fastshipmentsdev.backend_fastshipments.support.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Transactional(readOnly = false)
    public Spedizione aggiungi(Spedizione s) throws ClienteNonEsistenteException {

        Optional<Cliente> c = clienteRepository.findById(s.getMittente().getIdCliente());
        if(!c.isPresent()) throw new ClienteNonEsistenteException();

        return spedizioneRepository.save(s);
    }

    @Transactional(readOnly = false)
    public Spedizione ricerca(Integer idSpedizione) throws SpedizioneNonEsistenteException {

        Optional<Spedizione> s = spedizioneRepository.findById(idSpedizione);
        if(!s.isPresent()) throw new SpedizioneNonEsistenteException();
        return s.get();
    }


    public void pagamentoSpedizione(Integer idSpedizione, Integer idC, CartaCredito c){
        //MANCA ENTITY CARTA DI CREDITO
    }

    public Fattura generaFattura(Integer idspedizione, Integer idCliente){

        return null;
    }

    @Transactional(readOnly = true)
    public void spedizioneDaAbbonamento(Integer idAbbonamento, Integer idC, Spedizione s)
            throws SpedizioneNonEsistenteException, ClienteNonEsistenteException, AbbonamentoNonEsistenteException, AbbonamentoNonAssociatoException{

        Optional<Spedizione> spedizione = spedizioneRepository.findById(s.getIdSpedizione());
        if(!spedizione.isPresent()) throw new SpedizioneNonEsistenteException();

        Optional<Cliente> cliente = clienteRepository.findById(idC);
        if(!cliente.isPresent()) throw new ClienteNonEsistenteException();

        Optional<AbbonamentoSottoscritto> abbonamento = abbonamentoSottoscrittoRepository.findById(idAbbonamento);
        if(!abbonamento.isPresent()) throw new AbbonamentoNonEsistenteException();

        if(!cliente.get().getAbbonamenti().contains(abbonamento.get()))
            throw new AbbonamentoNonAssociatoException();

        Spedizione tmp = spedizione.get();
        tmp.setMittente(cliente.get());
        spedizioneRepository.save(tmp);


    }

    @Transactional(readOnly = true)
    public void spedizioneDaMagazzino(Integer idAbbonamentoMagazzino, Integer idC, List<Integer> idMerce)
            throws AbbonamentoNonEsistenteException, ClienteNonEsistenteException, MerceNonEsistenteException, MerceNonAssociataException {
        Optional<AbbonamentoMagazzinoSottoscritto> abbonamento = abbonamentoMagazzinoSottoscrittoRepository.findById(idAbbonamentoMagazzino);
        if(!abbonamento.isPresent())
            throw new AbbonamentoNonEsistenteException();
        Optional<Cliente> cliente = clienteRepository.findById(idC);
        if(!cliente.isPresent())
            throw new ClienteNonEsistenteException();

        for(Integer i : idMerce){
            Optional<Merce> tmp = merceRepository.findById(i);
            if(!tmp.isPresent() )
                throw new MerceNonEsistenteException();
            if(!(cliente.get().getMerceProprietario().contains(merceRepository.findById(i))))
                throw new MerceNonAssociataException();
        }

        Spedizione s = new Spedizione();
        s.setMittente(cliente.get());
        spedizioneRepository.save(s);


    }

    public void posticipaConsegna (Integer idSpedizione, LocalDateTime d) throws SpedizioneNonEsistenteException {
        Optional<Spedizione> spedizione = spedizioneRepository.findById(idSpedizione);
        if(!spedizione.isPresent())
            throw new SpedizioneNonEsistenteException();
        spedizione.get().setDataArrivo(d);
    }

}
