package com.fastshipmentsdev.backend_fastshipments.a_controller;

import com.fastshipmentsdev.backend_fastshipments.b_service.SpedizioneService;
import com.fastshipmentsdev.backend_fastshipments.d_entity.CartaCredito;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Merce;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Spedizione;
import com.fastshipmentsdev.backend_fastshipments.support.classi.Indirizzo;
import com.fastshipmentsdev.backend_fastshipments.support.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = "/spedizione")
public class SpedizioneController {

    @Autowired
    SpedizioneService spedizioneService;

    @PostMapping(value = "/aggiungi_spedizione/{idCliente}") //Todo da verificare
    public ResponseEntity aggiungi(@PathVariable int idCliente, @RequestBody Object[] spedAndCartaCredito){
        try {

            Spedizione s = (Spedizione)spedAndCartaCredito[0];
            CartaCredito cartaCredito = (CartaCredito) spedAndCartaCredito[1];
            Spedizione spedizione = spedizioneService.aggiungi(idCliente, s, cartaCredito);
            return new ResponseEntity(spedizione, HttpStatus.CREATED);
        }catch(ClienteNonEsistenteException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non esiste", e);
        } catch (PagamentoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore nel pagamento", e);
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
        } catch(ClienteNonEsistenteException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non esiste", e);
        }catch(AbbonamentoNonEsistenteException e ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonaento non esiste", e);
        }catch(AbbonamentoNonAssociatoException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'abbonamento' non è associato al cliente", e);
        } catch (SpedizioniTerminateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le spedizioni a sua disposizione per " +
                    "questo abbonamento sono terminate", e);
        }
    }

    @PostMapping(value="/spedizioneDaMagazzino/{idAbbonamentoMagazzino}/{idC}")     //todo da verifiare
    public void spedizioneDaMagazzino(@PathVariable Integer idAbbonamentoMagazzino,@PathVariable Integer idC,@RequestBody List<Object> indirizzoDestAndMerci){
        try{
            Indirizzo indirizzoDestinazione = (Indirizzo) indirizzoDestAndMerci.get(0);
            List<Integer> idMerci = new LinkedList<>();
            Iterator<Object> i = indirizzoDestAndMerci.iterator();
            while (i.hasNext()){
                idMerci.add((Integer)i.next());
            }
            spedizioneService.spedizioneDaMagazzino(idAbbonamentoMagazzino,idC, idMerci, indirizzoDestinazione);
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
        }
    }

    @PutMapping(value= "/posticipaConsegna/{idSpedizione}")//todo da controllare
    public void posticipaConsegna (@PathVariable Integer idSpedizione,@RequestBody LocalDateTime d){
        try{
            spedizioneService.posticipaConsegna(idSpedizione,d);
        }catch(SpedizioneNonEsistenteException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La spedizione non esiste", e);
        }
    }

    //TODO Lista spedizioni effettuate dal cliente
    @GetMapping(value = "/effettuate/{idCliente}")
    public ResponseEntity spedizioneEffettuate(@PathVariable int idCliente){
        try {
            return new ResponseEntity(spedizioneService.spedizioniEffettuate(idCliente),HttpStatus.OK);
        } catch (ClienteNonEsistenteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non esiste", e);
        }
    }
}
