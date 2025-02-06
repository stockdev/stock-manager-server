package mycode.stockmanager.app.stock.repository;

import mycode.stockmanager.app.stock.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Integer> {

    Optional<Stock> findById(Long id);

    Optional<Stock> findByOrderNumber(String orderNumber);
    @Modifying
    @Query(value = "UPDATE stock_sequence SET next_val = 1", nativeQuery = true)
    void resetStockSequence();

}
