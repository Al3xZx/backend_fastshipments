package com.fastshipmentsdev.backend_fastshipments.a_controller;

import com.fastshipmentsdev.backend_fastshipments.b_service.AnnuncioService;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Annuncio;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Candidato;
import com.fastshipmentsdev.backend_fastshipments.support.exception.AnnuncioNonEsistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/annuncio")
@CrossOrigin(origins="http://localhost:4200")
public class AnnuncioController {

    @Autowired
    AnnuncioService annuncioService;

    @PostMapping(value = "/aggiungi_candidato/{idAnnuncio}")
    public ResponseEntity aggiungiCandidato(@RequestBody Candidato c, @PathVariable int idAnnuncio){
        try {
            Candidato a = annuncioService.registraCandidato(c, idAnnuncio);
            return new ResponseEntity(a, HttpStatus.OK);
        } catch (AnnuncioNonEsistenteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Annuncio non esistente", e);
        }

    }
}
