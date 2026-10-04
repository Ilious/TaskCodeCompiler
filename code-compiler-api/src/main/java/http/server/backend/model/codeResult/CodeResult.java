package http.server.backend.model.codeResult;

import http.server.backend.model.task.Task;
import http.server.dto.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "code_results")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CodeResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "code_result", nullable = false)
    private String codeResult;

    @Column(name = "code_error", nullable = false)
    private String codeError;

    @OneToOne(mappedBy = "codeResult")
    private Task task;

    private Status status;

    private Long exitCode;
}
