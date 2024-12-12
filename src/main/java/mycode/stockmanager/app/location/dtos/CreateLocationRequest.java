package mycode.stockmanager.app.location.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateLocationRequest(@NotNull String name, @NotNull String code) {
}
