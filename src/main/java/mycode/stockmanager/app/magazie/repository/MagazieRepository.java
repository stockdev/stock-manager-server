package mycode.stockmanager.app.magazie.repository;

import mycode.stockmanager.app.magazie.model.Magazie;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface MagazieRepository extends JpaRepository<Magazie, Long> {

    Optional<Magazie> findByArticleCodeAndLocationCode(String articleCode, String locationCode);

    Optional<List<Magazie>> findAllByArticleCode(String articleCode);

}
