package mycode.stockmanager.app.utilaje.repository;

import mycode.stockmanager.app.utilaje.model.Utilaje;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilajeRepository extends JpaRepository<Utilaje, Long> {

    Optional<Utilaje> findUtilajeById(Long id);

}
