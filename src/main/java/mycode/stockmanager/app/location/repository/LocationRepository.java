package mycode.stockmanager.app.location.repository;


import mycode.stockmanager.app.location.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    Optional<Location> findById(Long id);

    Optional<Location> findByCode(String code);

    @Modifying
    @Query(value = "UPDATE location_sequence SET next_val = 1", nativeQuery = true)
    void resetLocationSequence();
}
