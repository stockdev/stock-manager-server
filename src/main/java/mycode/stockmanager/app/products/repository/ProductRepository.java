package mycode.stockmanager.app.products.repository;

import mycode.stockmanager.app.products.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
