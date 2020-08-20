package com.fastshipmentsdev.backend_fastshipments.a_controller;

import com.fastshipmentsdev.backend_fastshipments.b_service.AbbonamentoMagazzinoService;
import com.fastshipmentsdev.backend_fastshipments.d_entity.AbbonamentoMagazzinoSottoscritto;
import com.fastshipmentsdev.backend_fastshipments.d_entity.CartaCredito;
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
}
