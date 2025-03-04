package mycode.stockmanager.app.utilaje.repository;

import mycode.stockmanager.app.utilaje.model.Utilaj;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilajeRepository extends JpaRepository<Utilaj, Long> {

    Optional<Utilaj> findUtilajeById(Long id);

    Optional<Utilaj> findByCode(String code);

    Page<Utilaj> findByCodeContainingIgnoreCase(String code, Pageable pageable);

    Page<Utilaj> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
