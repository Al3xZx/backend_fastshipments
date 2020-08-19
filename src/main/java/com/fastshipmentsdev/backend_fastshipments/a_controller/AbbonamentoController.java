package com.fastshipmentsdev.backend_fastshipments.a_controller;

import com.fastshipmentsdev.backend_fastshipments.b_service.AbbonamentoService;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Abbonamento;
import com.fastshipmentsdev.backend_fastshipments.d_entity.AbbonamentoSottoscritto;
import com.fastshipmentsdev.backend_fastshipments.d_entity.CartaCredito;
import com.fastshipmentsdev.backend_fastshipments.support.exception.AbbonamentoNonEsistenteException;
import com.fastshipmentsdev.backend_fastshipments.support.exception.ClienteNonEsistenteException;
import com.fastshipmentsdev.backend_fastshipments.support.exception.PagamentoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/abbonamento")
public class AbbonamentoController {

    @Autowired
    AbbonamentoService abbonamentoService;

    @GetMapping(value = "/all_abbonamenti/{resForPage}/{page}")
    public ResponseEntity allAbbonamenti(int resForPage, int page){
        return new ResponseEntity(abbonamentoService.allAbbonamenti(resForPage,page), HttpStatus.OK);
    }

    @PostMapping(value = "/sottoscrivi/{idCliente}/{idAbbonamento}")
    public ResponseEntity sottoscriviAbbonamento(
            @PathVariable int idCliente, @PathVariable int idAbbonamento, @RequestBody CartaCredito cartaCredito){
        try {
            AbbonamentoSottoscritto a = abbonamentoService.sottoscriviAbbonamento(idCliente, idAbbonamento, cartaCredito);
            return new ResponseEntity(a, HttpStatus.CREATED);
        } catch (ClienteNonEsistenteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non esiste", e);
        } catch (AbbonamentoNonEsistenteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonamento non esiste", e);
        } catch (PagamentoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "errore nel pagamento", e);
        }
    }

    @GetMapping(value = "/sottoscritti/{idCliente}")
    public ResponseEntity abbonamentiSottoscritti(@PathVariable int idCliente){
        try {
            return new ResponseEntity(abbonamentoService.abbonamentiSottoscritti(idCliente),HttpStatus.OK);
        } catch (ClienteNonEsistenteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non esiste", e);
        }
    }

    //MANCANO le liste di abbonamenti sottoscritti dal cliente,
    //le liste di abbonamenti magazzino sottoscritti dal cliente,
    //le liste di spedizioni effettuate dal cliente,


//
//    @PostMapping(value = "/aggiungi")
//    public ResponseEntity aggiungiAbbonamento (@RequestBody Abbonamento a){
//        return new ResponseEntity(abbonamentoService.aggiungiAbbonamento(a), HttpStatus.OK);
//    }
//
//    @PutMapping(value = "/modifica/{idAbbonamento}")
//    public ResponseEntity modifica(@PathVariable int idAbbonamento, @RequestBody Abbonamento a){
//        try {
//            return new ResponseEntity(abbonamentoService.modifica(idAbbonamento, a), HttpStatus.ACCEPTED);
//        } catch (AbbonamentoNonEsistenteException e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonamento non esiste", e);
//        }
//    }
//
//    @PutMapping(value = "/elimina/{idAbbonamento}")
//    public ResponseEntity elimina (@PathVariable int idAbbonamento){
//        try {
//            abbonamentoService.elimina(idAbbonamento);
//            return new ResponseEntity(HttpStatus.ACCEPTED);
//        } catch (AbbonamentoNonEsistenteException e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonamento non esiste", e);
//        }
//    }
}
