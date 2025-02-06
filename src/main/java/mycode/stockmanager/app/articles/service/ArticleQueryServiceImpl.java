package mycode.stockmanager.app.articles.service;


import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.dtos.ArticleResponse;
import mycode.stockmanager.app.articles.dtos.ArticleResponseList;
import mycode.stockmanager.app.articles.exceptions.NoArticleFound;
import mycode.stockmanager.app.articles.mapper.ArticleMapper;
import mycode.stockmanager.app.articles.model.Article;
import mycode.stockmanager.app.articles.repository.ArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArticleQueryServiceImpl implements ArticleQueryService {

    private ArticleRepository articleRepository;

    public ArticleResponse getArticleByCode(String code) {
        Article article = articleRepository.findByCode(code)
                .orElseThrow(() -> new NoArticleFound("No article with this code found"));

        return ArticleMapper.articleToResponseDto(article);
    }

    @Override
    public ArticleResponseList getArticles(int page, int size, String searchTerm) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Article> articlePage;

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            articlePage = articleRepository.findAll(pageable);
        } else {
            articlePage = articleRepository.findByCodeContainingIgnoreCase(searchTerm, pageable);

            if (articlePage.isEmpty()) {
                articlePage = articleRepository.findByNameContainingIgnoreCase(searchTerm, pageable);
            }
        }

        if (articlePage.isEmpty()) {
            throw new NoArticleFound("No articles found");
        }

        List<ArticleResponse> responses = articlePage.getContent()
                .stream()
                .map(ArticleMapper::articleToResponseDto)
                .collect(Collectors.toList());

        return new ArticleResponseList(responses, articlePage.getNumber(), articlePage.getTotalPages(), articlePage.getTotalElements());
    }

}
