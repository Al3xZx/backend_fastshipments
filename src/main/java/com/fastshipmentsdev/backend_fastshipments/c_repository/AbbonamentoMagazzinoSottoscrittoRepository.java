package com.fastshipmentsdev.backend_fastshipments.c_repository;

import com.fastshipmentsdev.backend_fastshipments.d_entity.AbbonamentoMagazzinoSottoscritto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbbonamentoMagazzinoSottoscrittoRepository extends JpaRepository<AbbonamentoMagazzinoSottoscritto, Integer> {

}
