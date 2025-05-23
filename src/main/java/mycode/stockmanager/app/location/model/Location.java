package mycode.stockmanager.app.location.model;

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
@Table(name = "location")
@Entity(name = "Location")
public class Location {


    @Id
    @SequenceGenerator(name = "location_sequence", sequenceName = "location_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_sequence")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(
            name = "code",
            nullable = false,
            unique = true
    )
    private String code;


    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
            stock.setLocation(null);
        }
    }



}
