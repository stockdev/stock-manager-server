package mycode.stockmanager.app.stock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StockType {

    IN("in"),
    OUT("out");
    private final String stockType;
}
