package http.server.backend.model.session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class SessionDto {

    private String id;

    private Long user_id;

    private Instant stTime;

    private Duration duration;
}
