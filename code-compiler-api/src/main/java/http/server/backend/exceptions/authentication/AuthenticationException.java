package http.server.backend.exceptions.authentication;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthenticationException extends RuntimeException{

    private final HttpStatus status;

    public AuthenticationException(String msg, HttpStatus statusCode) {
        super(msg);
        status = statusCode;
    }
}
