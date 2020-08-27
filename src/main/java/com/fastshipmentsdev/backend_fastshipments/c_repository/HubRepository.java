package com.fastshipmentsdev.backend_fastshipments.c_repository;

import com.fastshipmentsdev.backend_fastshipments.d_entity.Abbonamento;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Hub;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HubRepository extends JpaRepository<Hub, Integer> {

    //Hub findByRegioneContaining(String regione);

    @Query(value = "SELECT * FROM hub", nativeQuery = true)
    List<Hub> selAllHub();
}
