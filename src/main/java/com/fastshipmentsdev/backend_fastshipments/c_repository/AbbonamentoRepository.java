package com.fastshipmentsdev.backend_fastshipments.c_repository;

import com.fastshipmentsdev.backend_fastshipments.d_entity.Abbonamento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbbonamentoRepository extends JpaRepository<Abbonamento, Integer> {
    @Query(value = "SELECT * FROM abbonamento", nativeQuery = true)
    List<Abbonamento> selAll(Pageable pageable);
}
