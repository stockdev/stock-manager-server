package mycode.stockmanager.app.notification.model;


import jakarta.persistence.*;
import lombok.*;
import mycode.stockmanager.app.notification.enums.NotificationType;
import mycode.stockmanager.app.users.model.User;

import static jakarta.persistence.GenerationType.SEQUENCE;

@AllArgsConstructor
@ToString
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
@Table(name = "notification")
@Entity(name = "Notification")
public class Notification {

    @Id
    @SequenceGenerator(
            name = "notification_sequence",
            sequenceName = "notification_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "notification_sequence"
    )

    @Column(
            name = "id"
    )
    private long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(name = "message", nullable = false)
    private String message;


    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
