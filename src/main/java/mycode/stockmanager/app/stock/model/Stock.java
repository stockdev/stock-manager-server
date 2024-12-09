package mycode.stockmanager.app.stock.model;


import jakarta.persistence.*;
import lombok.*;
import mycode.stockmanager.app.stock.enums.StockType;
import mycode.stockmanager.app.stock.enums.SubStockType;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@ToString
@Builder
@Table(name = "stock")
@Entity(name = "Stock")
public class Stock {

    @Id
    @SequenceGenerator(name = "stock_sequence", sequenceName = "stock_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_sequence")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(
            name = "order_number",
            nullable = false
    )
    private int orderNumber;

    @Column(
            name = "quantity",
            nullable = false
    )
    private int quantity;

    @Column(
            name = "necessary",
            nullable = false
    )
    private int necessary;

    @Column(
            name = "transaction_date",
            nullable = false
    )
    private LocalDateTime transactionDate;

    @Column(
            name = "comment",
            nullable = false
    )
    private String comment;

    @Column(
            name = "stock_type",
            nullable = false
    )
    private StockType stockType;

    @Column(
            name = "sub_stock_type",
            nullable = false
    )
    private SubStockType subStockType;
}
