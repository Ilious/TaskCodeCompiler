package http.server.backend.model.session;

import http.server.backend.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "st_time")
    private Instant stTime;

    private Duration duration;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
