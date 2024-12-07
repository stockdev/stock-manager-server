package mycode.stockmanager.app.products.model;



import jakarta.persistence.*;
import lombok.*;


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
            nullable = false
    )
    private String code;


    @Column(
            name = "name",
            nullable = false
    )
    private String name;






}
