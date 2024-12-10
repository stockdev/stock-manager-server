package mycode.stockmanager.app.stock.enums;


import lombok.Getter;

@Getter
public enum StockType {

    IN("in:stockIn"),
    OUT("out:stockOut");
    // Getter method (optional, since Lombok can handle it)
    private final String stockType;

    StockType(String stockType) {
        this.stockType = stockType;
    }

}
