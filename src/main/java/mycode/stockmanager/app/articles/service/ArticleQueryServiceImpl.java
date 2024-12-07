package mycode.stockmanager.app.articles.service;


import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.repository.ArticleRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ArticleQueryServiceImpl implements ArticleQueryService {

    private ArticleRepository productRepository;
}
