package mycode.stockmanager.app.location.dtos;

import lombok.Builder;
import mycode.stockmanager.app.stock.model.Stock;

@Builder
public record LocationResponse(String code, String name, Stock stock, Long id) {
}
