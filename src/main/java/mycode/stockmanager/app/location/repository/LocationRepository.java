package mycode.stockmanager.app.location.repository;

import mycode.stockmanager.app.location.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
