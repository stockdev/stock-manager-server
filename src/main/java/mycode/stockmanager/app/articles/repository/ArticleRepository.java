package mycode.stockmanager.app.articles.repository;

import mycode.stockmanager.app.articles.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    Optional<Article> findById(Long id);

    Optional<Article> findByCode(String code);
}
