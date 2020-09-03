package com.fastshipmentsdev.backend_fastshipments.c_repository;

import com.fastshipmentsdev.backend_fastshipments.d_entity.Abbonamento;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Merce;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerceRepository extends JpaRepository<Merce, Integer> {

    @Query(value = "SELECT count(*) " +
                    "FROM merce " +
                    "where merce.descrizione = ?1 " +
                    "group by descrizione", nativeQuery = true)
    Integer contaMerce(String descrizione);


    @Query(value = "SELECT count(*) " +
            "FROM merce " +
            "where merce.descrizione = ?1 AND merce.stato = 'STOCCATA' " +
            "group by descrizione", nativeQuery = true)
    Integer contaMerceStoccata(String descrizione);
}
