package http.server.backend.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class Session {

    private String id;

    private Long user_id;

    private Instant stTime;

    private Duration duration;
}
