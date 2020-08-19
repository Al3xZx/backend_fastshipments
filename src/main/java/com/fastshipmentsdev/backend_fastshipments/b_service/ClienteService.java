package com.fastshipmentsdev.backend_fastshipments.b_service;

import com.fastshipmentsdev.backend_fastshipments.c_repository.AccountClienteRepository;
import com.fastshipmentsdev.backend_fastshipments.c_repository.ClienteRepository;
import com.fastshipmentsdev.backend_fastshipments.d_entity.AccountCliente;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Cliente;
import com.fastshipmentsdev.backend_fastshipments.support.exception.ClienteNonEsistenteException;
import com.fastshipmentsdev.backend_fastshipments.support.exception.ClienteNonRegistratoException;
import com.fastshipmentsdev.backend_fastshipments.support.exception.PasswordErrataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    AccountClienteRepository accountClienteRepository;

    @Transactional(readOnly = false)
    public AccountCliente registraCliente(Cliente c, String password) {
        AccountCliente accountCliente = new AccountCliente();
        accountCliente.setUsername(c);
        accountCliente.setPassword(password);
        clienteRepository.save(c);
        accountClienteRepository.save(accountCliente);
        return accountCliente;
    }

    @Transactional(readOnly = true)
    public Cliente accedi (AccountCliente a) throws ClienteNonEsistenteException, ClienteNonRegistratoException, PasswordErrataException {
        Integer idCliente = a.getUsername().getIdCliente();
        Optional<Cliente> oc = clienteRepository.findById(idCliente);
        if(!oc.isPresent()) throw new ClienteNonEsistenteException();
        AccountCliente accountCliente = accountClienteRepository.findByIdCliente(idCliente);
        if(accountCliente == null) throw new ClienteNonRegistratoException();
        if(!a.getPassword().equals(accountCliente.getPassword()))
            throw new PasswordErrataException();
        return accountCliente.getUsername();
    }
}
