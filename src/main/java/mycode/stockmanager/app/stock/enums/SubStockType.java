package mycode.stockmanager.app.stock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public enum SubStockType {

    F("f:furnizor"),
    RP("rp"),
    P("p:productie");
    private final String subStockType;

    SubStockType(String subStockType) {
        this.subStockType = subStockType;
    }
}
