package com.fastshipmentsdev.backend_fastshipments.c_repository;

import com.fastshipmentsdev.backend_fastshipments.d_entity.Annuncio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnuncioRepository extends JpaRepository<Annuncio, Integer> {
}
