package mycode.stockmanager.app.stock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SubStockType {

    F("f:furnizor"),
    RP("rp"),
    P("p:productie");
    private final String subStockType;
}
