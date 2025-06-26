package http.server.backend.exceptions.authentication;

import lombok.Getter;

@Getter
public class LoginException extends RuntimeException {

    private final String password;

    public LoginException(String message, String password) {
        super(message);
        this.password = password;
    }
}
