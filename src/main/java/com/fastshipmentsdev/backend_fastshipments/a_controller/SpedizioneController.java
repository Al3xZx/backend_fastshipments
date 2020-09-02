package com.fastshipmentsdev.backend_fastshipments.a_controller;

import com.fastshipmentsdev.backend_fastshipments.b_service.SpedizioneService;
import com.fastshipmentsdev.backend_fastshipments.d_entity.CartaCredito;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Merce;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Spedizione;
import com.fastshipmentsdev.backend_fastshipments.support.classi.DateTime;
import com.fastshipmentsdev.backend_fastshipments.support.classi.Indirizzo;
import com.fastshipmentsdev.backend_fastshipments.support.classi.IndirizzoIdMerci;
import com.fastshipmentsdev.backend_fastshipments.support.classi.SpedizioCartaWrap;
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
@CrossOrigin(origins = "http://localhost:4200")
public class SpedizioneController {

    @Autowired
    SpedizioneService spedizioneService;

    @PostMapping(value = "/aggiungi_spedizione/{idCliente}")
    public ResponseEntity aggiungi(@PathVariable int idCliente, @RequestBody SpedizioCartaWrap SCW){
        try {
            Spedizione spedizione = SCW.getSpedizione();
            CartaCredito cartaCredito = SCW.getCartaCredito();
            Spedizione spedizioneRet = spedizioneService.aggiungi(idCliente, spedizione, cartaCredito);
            return new ResponseEntity(spedizioneRet, HttpStatus.CREATED);
        }catch(ClienteNonEsistenteException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non esiste", e);
        } catch (PagamentoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore nel pagamento", e);
        } catch (ServizioInZonaNonDisponibileException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il servizio non è erogato nella regione scelta", e);
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


    @PostMapping(value="/spedizioneDaAbbonamento/{idAbbonamento}/{idC}") //todo test
    public ResponseEntity spedizioneDaAbbonamento(@PathVariable int idAbbonamento,@PathVariable int idC,@RequestBody Spedizione s){
        try{
            return new ResponseEntity(spedizioneService.spedizioneDaAbbonamento(idAbbonamento,idC,s), HttpStatus.OK);
        } catch(ClienteNonEsistenteException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non esiste", e);
        }catch(AbbonamentoNonEsistenteException e ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonaento non esiste", e);
        }catch(AbbonamentoNonAssociatoException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonamento' non è associato al cliente", e);
        } catch (SpedizioniTerminateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le spedizioni a sua disposizione per " +
                    "questo abbonamento sono terminate", e);
        } catch (AbbonamentoScadutoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonamento' è scaduto", e);
        } catch (ServizioInZonaNonDisponibileException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il servizio non è erogato nella regione scelta", e);
        }
    }

    @PostMapping(value="/spedizioneDaMagazzino/{idAbbonamentoMagazzino}/{idC}")//todo test
    public ResponseEntity spedizioneDaMagazzino(@PathVariable Integer idAbbonamentoMagazzino, @PathVariable Integer idC,@RequestBody IndirizzoIdMerci indirizzoDestAndMerci){
        try{
            Indirizzo indirizzoDestinazione = indirizzoDestAndMerci.getIndirizzo();
            List<Integer> idMerci = indirizzoDestAndMerci.getIdMerci();
            Spedizione s = spedizioneService.spedizioneDaMagazzino(idAbbonamentoMagazzino,idC, idMerci, indirizzoDestinazione);
            return new ResponseEntity(s, HttpStatus.OK);
        }catch(MerceNonEsistenteException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La merce non esiste", e);
        }catch(ClienteNonEsistenteException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non esiste", e);
        }catch(AbbonamentoNonEsistenteException e ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonaento non esiste", e);
        }catch(MerceNonAssociataException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La merce non è  associata al cliente", e);
        } catch (AbbonamentoNonAssociatoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonamento' non è associato al cliente", e);
        } catch (MerceNonDisponibileException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La merce non è disponibile poichè già in lavorazione per un'altra spedizione", e);
        } catch (MerceNonStoccataException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La merce non è  ancora disponibile per la lavorazione alla spedizione", e);
        } catch (ServizioInZonaNonDisponibileException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il servizio non è erogato nella regione scelta", e);
        }
    }

    @PutMapping(value= "/posticipaConsegna/{idSpedizione}")
    public ResponseEntity posticipaConsegna (@PathVariable Integer idSpedizione,@RequestBody LocalDateTime d){
        try{
            System.out.println(d);
            Spedizione s = spedizioneService.posticipaConsegna(idSpedizione,d);
            return new ResponseEntity(s, HttpStatus.OK);
        }catch(SpedizioneNonEsistenteException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La spedizione non esiste", e);
        } catch (DateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La data è inferiore della data di consegna", e);
        }
    }

    @GetMapping(value = "/effettuate/{idCliente}")
    public ResponseEntity spedizioneEffettuate(@PathVariable int idCliente){
        try {
            return new ResponseEntity(spedizioneService.spedizioniEffettuate(idCliente),HttpStatus.OK);
        } catch (ClienteNonEsistenteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non esiste", e);
        }
    }
}
