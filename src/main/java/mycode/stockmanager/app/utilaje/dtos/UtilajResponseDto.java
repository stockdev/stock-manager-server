package mycode.stockmanager.app.utilaje.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UtilajResponseDto(@NotNull String code,@NotNull String name) {
}
