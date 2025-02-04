package mycode.stockmanager.app.magazie.repository;

import mycode.stockmanager.app.magazie.model.Magazie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MagazieRepository extends JpaRepository<Magazie, Long> {
}
