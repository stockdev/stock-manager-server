package mycode.stockmanager.app.articles.repository;

import mycode.stockmanager.app.articles.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
}
