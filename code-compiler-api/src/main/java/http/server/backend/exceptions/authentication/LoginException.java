package http.server.backend.exceptions.authentication;

import lombok.Getter;

@Getter
public class LoginException extends RuntimeException {

    private final String login;

    public LoginException(String message, String login) {
        super(message);
        this.login = login;
    }
}
