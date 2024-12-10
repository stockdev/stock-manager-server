package mycode.stockmanager.app.articles.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateArticleRequest(@NotNull String name, @NotNull String code) {
}
