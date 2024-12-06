package mycode.stockmanager.app.products.web;


import lombok.AllArgsConstructor;
import mycode.stockmanager.app.products.service.ProductCommandService;
import mycode.stockmanager.app.products.service.ProductQueryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock-manager/api/product")
@AllArgsConstructor
public class ProductController {


    private ProductCommandService productCommandService;
    private ProductQueryService productQueryService;


}
