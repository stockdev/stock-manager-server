package mycode.stockmanager.app.location.repository;


import mycode.stockmanager.app.location.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    Optional<Location> findById(Long id);

    Optional<Location> findByCode(String code);
}
