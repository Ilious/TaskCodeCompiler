package http.server.backend.exceptions.Handlers;

import http.server.backend.exceptions.authentication.LoginException;
import http.server.backend.model.api.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class LoginExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError getLoginHandler(LoginException authException) {
        String errMessage = String.format("Authentication exception password [%s] is not correct",
                authException.getPassword());

        log.warn("{}:\n {}", errMessage, authException.getMessage());
        return ApiError.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .description(errMessage)
                .build();
    }
}
