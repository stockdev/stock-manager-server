package mycode.stockmanager.app.stock.repository;

import mycode.stockmanager.app.stock.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Integer> {

    Optional<Stock> findById(Long id);

    Optional<Stock> findByOrderNumber(String orderNumber);

}
