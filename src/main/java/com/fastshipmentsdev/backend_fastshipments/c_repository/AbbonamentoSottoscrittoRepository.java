package com.fastshipmentsdev.backend_fastshipments.c_repository;

import com.fastshipmentsdev.backend_fastshipments.d_entity.AbbonamentoSottoscritto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbbonamentoSottoscrittoRepository extends JpaRepository<AbbonamentoSottoscritto, Integer> {
}
