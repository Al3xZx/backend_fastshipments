package com.fastshipmentsdev.backend_fastshipments.a_controller;

import com.fastshipmentsdev.backend_fastshipments.b_service.AbbonamentoService;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Abbonamento;
import com.fastshipmentsdev.backend_fastshipments.d_entity.AbbonamentoSottoscritto;
import com.fastshipmentsdev.backend_fastshipments.d_entity.CartaCredito;
import com.fastshipmentsdev.backend_fastshipments.support.exception.AbbonamentoNonEsistenteException;
import com.fastshipmentsdev.backend_fastshipments.support.exception.ClienteNonEsistenteException;
import com.fastshipmentsdev.backend_fastshipments.support.exception.PagamentoException;
import com.fastshipmentsdev.backend_fastshipments.support.exception.ServizioInZonaNonDisponibileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/abbonamento")
@CrossOrigin(origins="http://localhost:4200")
public class AbbonamentoController {

    @Autowired
    AbbonamentoService abbonamentoService;

    @GetMapping(value = "/all_abbonamenti/{resForPage}/{page}")
    public ResponseEntity allAbbonamenti(@PathVariable int resForPage, @PathVariable int page){
        return new ResponseEntity(abbonamentoService.allAbbonamenti(resForPage,page), HttpStatus.OK);
    }

    @PostMapping(value = "/sottoscrivi/{idCliente}/{idAbbonamento}") //Todo da verificare
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
        } catch (ServizioInZonaNonDisponibileException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "il servizio da lei scelto non " +
                    "è erogato nella regione in cui risiede", e);
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
}
