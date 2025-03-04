package mycode.stockmanager.app.articles.service;


import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.dtos.*;
import mycode.stockmanager.app.articles.exceptions.NoArticleFound;
import mycode.stockmanager.app.articles.mapper.ArticleMapper;
import mycode.stockmanager.app.articles.model.Article;
import mycode.stockmanager.app.articles.repository.ArticleRepository;
import mycode.stockmanager.app.stock.enums.StockType;
import mycode.stockmanager.app.stock.model.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public MagazieTotalResponse getMagazieTotalForArticle(String articleCode) {
        Article article = articleRepository.findByCode(articleCode)
                .orElseThrow(() -> new NoArticleFound("No article with this code found"));

        int stockIn = 0, stockOut = 0, finalStock, stockProduction = 0;

        List<MagaziePrintResponse> list = getMagaziePrintResponses(article);


        if(!article.getStocks().isEmpty()){
            List<Stock> stocks = article.getStocks();

            for (Stock stock : stocks) {
                if(stock.getStockType().equals(StockType.IN)){
                    stockIn+= stock.getQuantity();
                }else if(stock.getStockType().equals(StockType.OUT)){
                    stockOut+= stock.getQuantity();
                    stockProduction+= stock.getNecessary();
                }
            }
        }else{
            throw new NoArticleFound("Article has no stock transactions");
        }

        finalStock = stockIn - stockOut;


        return MagazieTotalResponse.builder()
                .articleCode(article.getCode())
                .articleName(article.getName())
                .finalStock(finalStock)
                .stockIn(stockIn)
                .stockOut(stockOut)
                .locations(list)
                .stockProduction(stockProduction).build();
    }

    private static List<MagaziePrintResponse> getMagaziePrintResponses(Article article) {
        List<MagaziePrintResponse> list = new ArrayList<>();

        if(!article.getStocks().isEmpty()){
            List<Stock> stocks = article.getStocks();

            stocks.forEach(stock -> {
                if(stock.getStockType().equals(StockType.IN)) {
                    list.add(MagaziePrintResponse.builder()
                            .locationCode(stock.getLocation().getCode())
                            .stock(stock.getQuantity()).build());
                }
            });
        }else{
            throw new NoArticleFound("Article has no stock transactions");
        }
        return list;
    }

}
