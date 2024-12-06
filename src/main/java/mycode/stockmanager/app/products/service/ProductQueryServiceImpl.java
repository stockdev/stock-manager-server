package mycode.stockmanager.app.products.service;


import lombok.AllArgsConstructor;
import mycode.stockmanager.app.products.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductQueryServiceImpl implements ProductQueryService{

    private ProductRepository productRepository;
}
