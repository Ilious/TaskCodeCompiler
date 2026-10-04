package http.server.backend.model.task;

import http.server.backend.model.codeResult.CodeResult;
import http.server.backend.model.user.User;
import http.server.dto.enums.Compiler;
import http.server.dto.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private Compiler compiler;

    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "code_result_id")
    private CodeResult codeResult;
}
