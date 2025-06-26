package http.server.backend.exceptions.authentication;

import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException{

    private final String bearerToken;

    public AuthenticationException(String msg, String bearerToken) {
        super(msg);
        this.bearerToken = bearerToken;
    }
}
