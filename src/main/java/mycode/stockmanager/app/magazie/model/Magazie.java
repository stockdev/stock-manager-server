package mycode.stockmanager.app.magazie.model;


import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@ToString
@Builder
@Table(name = "magazie")
@Entity(name = "Magazie")
public class Magazie {


    @Id
    @SequenceGenerator(name = "magazie_sequence", sequenceName = "magazie_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "magazie_sequence")
    @Column(name = "id", nullable = false)
    private Long id;


}
