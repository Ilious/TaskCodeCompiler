package http.server.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("app.session")
public class SessionConfig {

    private Long ttlMinutes;

    private Long cleanIntervalHours;
}
