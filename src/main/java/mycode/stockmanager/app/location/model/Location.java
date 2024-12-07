package mycode.stockmanager.app.location.model;

import jakarta.persistence.*;
import lombok.*;

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






}
