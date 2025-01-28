package mycode.stockmanager.app.utilaje.model;


import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@ToString
@Builder
@Table(name = "utilaje")
@Entity(name = "Utilaje")
public class Utilaj {

    @Id
    @SequenceGenerator(name = "utilaje_sequence", sequenceName = "utilaje_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "utilaje_sequence")
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(
            name = "name",
            nullable = false
    )
    private String name;

}
