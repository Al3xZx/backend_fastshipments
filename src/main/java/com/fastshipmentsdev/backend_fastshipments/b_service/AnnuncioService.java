package com.fastshipmentsdev.backend_fastshipments.b_service;

import com.fastshipmentsdev.backend_fastshipments.c_repository.AnnuncioRepository;
import com.fastshipmentsdev.backend_fastshipments.c_repository.CandidatoRepository;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Annuncio;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Candidato;
import com.fastshipmentsdev.backend_fastshipments.support.exception.AnnuncioNonEsistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnuncioService {

    @Autowired
    AnnuncioRepository annuncioRepository;

    @Autowired
    CandidatoRepository candidatoRepository;

    public Candidato registraCandidato(Candidato c, int idAnnuncio) throws AnnuncioNonEsistenteException {
        Optional<Annuncio> oA = annuncioRepository.findById(idAnnuncio);
        if(!oA.isPresent())
            throw new AnnuncioNonEsistenteException();
        Annuncio a = oA.get();
        c.setIndirizzo(c.getIndirizzoCandidato().toString());
        c.setAnnuncio(a);
        candidatoRepository.save(c);
        a.getCandidati().add(c);
        return c;
    }

    public List<Annuncio> allAnnunci(){
        return annuncioRepository.findAll();
    }


}
