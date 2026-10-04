package http.server.queue.config.docker;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("app.docker.compiler")
public class DockerCompilerConfig {

    private String path;

    private Long timeoutInSeconds;

    private Long imageBuiltTimeout;

    private Long maxOutputBytes;
}
