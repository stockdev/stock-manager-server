package mycode.stockmanager.app.stock.repository;

import mycode.stockmanager.app.stock.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Integer> {
}
