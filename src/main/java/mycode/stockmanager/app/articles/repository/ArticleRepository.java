package mycode.stockmanager.app.articles.repository;

import mycode.stockmanager.app.articles.dtos.ArticleResponseList;
import mycode.stockmanager.app.articles.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    Optional<Article> findById(Long id);

    Optional<Article> findByCode(String code);

    @Modifying
    @Query(value = "UPDATE article_sequence SET next_val = 1", nativeQuery = true)
    void resetArticleIdSequence();


    Page<Article> findByCodeContainingIgnoreCase(String code, Pageable pageable);




}
