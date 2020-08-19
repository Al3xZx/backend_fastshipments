package com.fastshipmentsdev.backend_fastshipments.c_repository;

import com.fastshipmentsdev.backend_fastshipments.d_entity.AccountCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface AccountClienteRepository extends JpaRepository<AccountCliente, Integer> {

    @Query(value = "SELECT * FROM account_cliente AS a WHERE a.username_id_cliente = ?1", nativeQuery = true)
    AccountCliente findByIdCliente(Integer idCliente);

}
