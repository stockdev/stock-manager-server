package mycode.stockmanager.app.utilaje.repository;

import mycode.stockmanager.app.utilaje.model.Utilaj;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UtilajeRepository extends JpaRepository<Utilaj, Long> {
    @Modifying
    @Query(value = "UPDATE utilaje_sequence SET next_val = 1", nativeQuery = true)
    void resetUtilajeIdSequence();


    Optional<Utilaj> findByCode(String code);

    Page<Utilaj> findByCodeContainingIgnoreCase(String code, Pageable pageable);

    Page<Utilaj> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
