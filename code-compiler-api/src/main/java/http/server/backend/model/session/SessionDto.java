package http.server.backend.model.session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class SessionDto {

    private UUID id;

    private Long userId;

    private Instant createdAt;

    private Instant expiresAt;
}
