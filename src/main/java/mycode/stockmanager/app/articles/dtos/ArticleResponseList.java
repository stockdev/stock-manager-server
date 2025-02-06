package mycode.stockmanager.app.articles.dtos;

import java.util.List;

public record ArticleResponseList(
        List<ArticleResponse> list,
        int currentPage,
        int totalPages,
        long totalElements
) {
}
