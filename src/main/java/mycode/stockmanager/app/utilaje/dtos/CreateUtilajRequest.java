package mycode.stockmanager.app.utilaje.dtos;

import jakarta.validation.constraints.NotNull;

public record CreateUtilajRequest(@NotNull String name) {
}
