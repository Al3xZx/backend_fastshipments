package com.fastshipmentsdev.backend_fastshipments.a_controller;

import com.fastshipmentsdev.backend_fastshipments.b_service.SpedizioneService;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Spedizione;
import com.fastshipmentsdev.backend_fastshipments.support.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/spedizione")
public class SpedizioneController {

    @Autowired
    SpedizioneService spedizioneService;

    @PostMapping(value = "/aggiungi_spedizione")
    public ResponseEntity aggiungi(@RequestBody Spedizione s){
        try {
            Spedizione spedizione = spedizioneService.aggiungi(s);
            return new ResponseEntity(spedizione, HttpStatus.CREATED);
        }catch(ClienteNonEsistenteException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non esiste", e);
        }
    }

    @GetMapping(value ="/ricerca_spedizione/{idSpedizione}")
    public ResponseEntity ricerca(@PathVariable Integer idSpedizione) {

        try{
            Spedizione spedizione = spedizioneService.ricerca(idSpedizione);
            return new ResponseEntity(spedizione, HttpStatus.OK);

        }catch (SpedizioneNonEsistenteException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La Spedizione non esiste", e);
        }
    }


    @GetMapping(value="/spedizioneDaAbbonamento/{idAbbonamento}/{idC}")
    public void spedizioneDaAbbonamento(@PathVariable Integer idAbbonamento,@PathVariable Integer idC,@RequestBody Spedizione s){
        try{
            spedizioneService.spedizioneDaAbbonamento(idAbbonamento,idC,s);
        }catch(SpedizioneNonEsistenteException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La Spedizione non esiste", e);
        }catch(ClienteNonEsistenteException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non esiste", e);
        }catch(AbbonamentoNonEsistenteException e ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonaento non esiste", e);
        }catch(AbbonamentoNonAssociatoException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonamento' non è associato al cliente", e);
        }
    }

    @GetMapping(value="/spedizioneDaMagazzino/{idAbbonamentoMagazzino}/{idC}/{idMerce}")
    public void spedizioneDaMagazzino(@PathVariable Integer idAbbonamentoMagazzino,@PathVariable Integer idC,@PathVariable List<Integer> idMerce){
        try{
            spedizioneService.spedizioneDaMagazzino(idAbbonamentoMagazzino,idC, idMerce);
        }catch(MerceNonEsistenteException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La merce non esiste", e);
        }catch(ClienteNonEsistenteException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non esiste", e);
        }catch(AbbonamentoNonEsistenteException e ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonaento non esiste", e);
        }catch(MerceNonAssociataException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La merce non è  associata al cliente", e);
        }
    }

    @PutMapping(value= "/posticipaConsegna/{idSpedizione}")
    public void posticipaConsegna (@PathVariable Integer idSpedizione,@RequestBody LocalDateTime d){
        try{
            spedizioneService.posticipaConsegna(idSpedizione,d);
        }catch(SpedizioneNonEsistenteException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La spedizione non esiste", e);
        }
    }

    //Lista spedizioni effettuate dal cliente
}
