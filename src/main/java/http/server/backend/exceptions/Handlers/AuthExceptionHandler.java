package http.server.backend.exceptions.Handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<String> getLoginHandler(AuthenticationServiceException authException) {
        log.warn("AuthenticationServiceException: {}", authException.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(authException.getMessage());
    }
}
