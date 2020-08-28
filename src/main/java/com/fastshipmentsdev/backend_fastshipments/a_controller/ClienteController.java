package com.fastshipmentsdev.backend_fastshipments.a_controller;

import com.fastshipmentsdev.backend_fastshipments.b_service.ClienteService;
import com.fastshipmentsdev.backend_fastshipments.d_entity.AccountCliente;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Cliente;
import com.fastshipmentsdev.backend_fastshipments.support.exception.ClienteNonEsistenteException;
import com.fastshipmentsdev.backend_fastshipments.support.exception.ClienteNonRegistratoException;
import com.fastshipmentsdev.backend_fastshipments.support.exception.PasswordErrataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/accounting")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @PostMapping(value = "/registra_cliente/{password}")
    public ResponseEntity registrazione(@RequestBody Cliente cliente, @PathVariable String password){
        AccountCliente c = clienteService.registraCliente(cliente, password);
        return new ResponseEntity(c, HttpStatus.CREATED);
    }

    @PostMapping(value = "/verifica_accesso")
    public ResponseEntity accedi(@RequestBody AccountCliente a) {
        try {
            return new ResponseEntity(clienteService.accedi(a), HttpStatus.OK);
        } catch (ClienteNonEsistenteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non esiste", e);
        } catch (ClienteNonRegistratoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il cliente non è registrato", e);
        } catch (PasswordErrataException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password errata", e);
        }
    }

}
