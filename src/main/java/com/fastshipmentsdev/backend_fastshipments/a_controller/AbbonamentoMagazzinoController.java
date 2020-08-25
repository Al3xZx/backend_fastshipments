package com.fastshipmentsdev.backend_fastshipments.a_controller;

import com.fastshipmentsdev.backend_fastshipments.b_service.AbbonamentoMagazzinoService;
import com.fastshipmentsdev.backend_fastshipments.d_entity.AbbonamentoMagazzinoSottoscritto;
import com.fastshipmentsdev.backend_fastshipments.d_entity.CartaCredito;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Merce;
import com.fastshipmentsdev.backend_fastshipments.support.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/abbonamento_magazzino")
public class AbbonamentoMagazzinoController {

    @Autowired
    AbbonamentoMagazzinoService abbonamentoMagazzinoService;

    @GetMapping(value = "/all_abbonamenti/{resForPage}/{page}")
    public ResponseEntity allAbbonamenti(int resForPage, int page){
        return new ResponseEntity(abbonamentoMagazzinoService.allAbbonamentiMagazzino(resForPage,page), HttpStatus.OK);
    }

    @PostMapping(value = "/sottoscrivi/{idCliente}/{idAbbonamento}/{idHub}")
    public ResponseEntity sottoscriviAbbonamento(
            @PathVariable int idCliente, @PathVariable int idAbbonamento, @PathVariable int idHub, @RequestBody CartaCredito cartaCredito){
        try {
            AbbonamentoMagazzinoSottoscritto a;
            a = abbonamentoMagazzinoService.sottoscriviAbbonamento(idCliente, idAbbonamento, idHub, cartaCredito);
            return new ResponseEntity(a, HttpStatus.CREATED);
        } catch (ClienteNonEsistenteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non esiste", e);
        } catch (AbbonamentoNonEsistenteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonamento non esiste", e);
        } catch (PagamentoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "errore nel pagamento", e);
        } catch (SpazioNonDisponibileException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'hub scelto non dispone dello " +
                    "spazio necessario per soffisfare quello previsto nell'abbonamento scelto", e);
        } catch (HubNonEsistenteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'hub scelto non esiste", e);
        }
    }

    @GetMapping(value = "/sottoscritti/{idCliente}")
    public ResponseEntity abbonamentiSottoscritti(@PathVariable int idCliente){
        try {
            return new ResponseEntity(abbonamentoMagazzinoService.abbonamentiMagazzinoSottoscritti(idCliente),HttpStatus.OK);
        } catch (ClienteNonEsistenteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non esiste", e);
        }
    }

    @PostMapping(value = "/richiesta_ritiro/{idCliente}/{idAbbonamentoMagazzinoSottoscritto}/{numeroMerci}")
    public ResponseEntity richiestaRitiroMerce(@PathVariable int idCliente, @PathVariable int idAbbonamentoMagazzinoSottoscritto, @RequestBody Merce merce, @PathVariable int numeroMerci){
        try {
            return new ResponseEntity(abbonamentoMagazzinoService.richiestaRitiroMerce(idCliente, idAbbonamentoMagazzinoSottoscritto, merce, numeroMerci), HttpStatus.OK);
        } catch (ClienteNonEsistenteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non esiste", e);
        } catch (AbbonamentoNonEsistenteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonamento non esiste", e);
        } catch (SpazioNonDisponibileException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Non c'è spazio a sufficienza con l'abbonamento sottoscritto", e);
        } catch (AbbonamentoNonAssociatoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonamento non è associato al cliente", e);
        } catch (AbbonamentoScadutoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonamento' è scaduto", e);
        }
    }

    @GetMapping(value = "/ricerca_merce/{idCliente}/{descrizione}")
    public ResponseEntity ricercaMerceDescr(@PathVariable int idCliente, @PathVariable String descrizione){
        try {
            return new ResponseEntity(abbonamentoMagazzinoService.ricercaMerceDescr(idCliente, descrizione), HttpStatus.OK);
        } catch (ClienteNonEsistenteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non esiste", e);
        }
    }

    @GetMapping(value = "/merce_in_magazzino/{idCliente}/{idAbbonamento}")
    public ResponseEntity merceInMagazzino(@PathVariable int idCliente, @PathVariable int idAbbonamento){//todo test....
        try {
            return new ResponseEntity(abbonamentoMagazzinoService.merceInMagazzino(idCliente, idAbbonamento), HttpStatus.OK);
        } catch (ClienteNonEsistenteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non esiste", e);
        } catch (AbbonamentoNonEsistenteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonamento non esiste", e);
        } catch (AbbonamentoNonAssociatoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonamento non è associato al cliente", e);
        }
    }
}
