package mycode.stockmanager.app.location.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateLocationRequest(@NotNull String code) {
}
