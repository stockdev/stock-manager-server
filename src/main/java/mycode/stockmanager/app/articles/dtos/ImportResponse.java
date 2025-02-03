package mycode.stockmanager.app.articles.dtos;


import java.util.List;

public record ImportResponse(int importedCount, List<String> skippedRows) {
}

