package mycode.stockmanager.app.location.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import mycode.stockmanager.app.products.model.Product;

import java.util.HashSet;
import java.util.Set;

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
            nullable = false
    )
    private String code;



    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @JsonManagedReference
    private Set<Product> products = new HashSet<>();


}
