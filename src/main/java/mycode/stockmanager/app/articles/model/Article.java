package mycode.stockmanager.app.articles.model;



import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import mycode.stockmanager.app.stock.model.Stock;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@ToString
@Builder
@Table(name = "article")
@Entity(name = "Article")
public class Article {

    @Id
    @SequenceGenerator(name = "article_sequence", sequenceName = "article_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_sequence")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(
            name = "code",
            nullable = false,
            unique = true
    )
    private String code;


    @Column(
            name = "name",
            nullable = false
    )
    private String name;


    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Stock> stocks;

    public void addStock(Stock stock) {
        if (stock != null) {
            if (stocks == null) {
                stocks = new ArrayList<>();
            }
            stocks.add(stock);
        }
    }
    public void removeStock(Stock stock) {
        if (stock != null && stocks != null) {
            stocks.remove(stock);
            stock.setArticle(null);
        }
    }

}
