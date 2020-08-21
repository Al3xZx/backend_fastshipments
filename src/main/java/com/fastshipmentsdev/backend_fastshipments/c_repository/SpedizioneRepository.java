package com.fastshipmentsdev.backend_fastshipments.c_repository;

import com.fastshipmentsdev.backend_fastshipments.d_entity.Cliente;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Spedizione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpedizioneRepository extends JpaRepository<Spedizione, Integer>{
    Spedizione findByMittente(Cliente c);

    List<Spedizione> findAllByMittente(Cliente cliente);
}
