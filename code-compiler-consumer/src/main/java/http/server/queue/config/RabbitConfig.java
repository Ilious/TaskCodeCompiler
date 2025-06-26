package http.server.queue.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter @Setter
@ConfigurationProperties("broker")
public class RabbitConfig {

    private String queue;

    private String exchange;

    private String key;

    private Login login;

    @Getter @Setter
    public static class Login {

        private String username;

        private String password;

        private String address;

        private int port;
    }
}
