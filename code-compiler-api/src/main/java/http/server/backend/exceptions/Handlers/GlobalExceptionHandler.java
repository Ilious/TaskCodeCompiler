package http.server.backend.exceptions.Handlers;


import http.server.backend.model.api.ApiError;
import http.server.backend.exceptions.storage.EntityCreateException;
import http.server.backend.exceptions.storage.EntityExistsException;
import http.server.backend.exceptions.storage.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError getEntityExistsHandler(EntityExistsException entityExistsException) {
        String errMessage = String.format("Entity [%s]: [%s] already exists exception",
                entityExistsException.getModel(), entityExistsException.getValue());
        log.warn("{}:\n {}", errMessage, entityExistsException.getMessage());
        return ApiError.builder()
                .code(HttpStatus.CONFLICT.value())
                .description(errMessage)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError getEntityNotFoundHandler(EntityNotFoundException entityNotFoundException) {
        String errMessage = String.format("Entity [%s]: [%s] is not found exception",
                entityNotFoundException.getModel(), entityNotFoundException.getValue());
        log.warn("{}:\n {}", errMessage, entityNotFoundException.getMessage());
        return ApiError.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .description(errMessage)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError getEntityCreateHandler(EntityCreateException entityFoundException) {
        String errMessage = "Entity creating exception";
        log.warn("{}:\n {}", errMessage, entityFoundException.getMessage());
        return ApiError.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .description(errMessage)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationAnnotationException(MethodArgumentNotValidException exception) {
        StringBuilder errMessage = new StringBuilder("Input fields aren't correct: ");
        String errorFields = exception.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getField)
                .collect(Collectors.joining(", "));
        errMessage.append(errorFields);

        log.warn("{}\n {}", errMessage, exception.getMessage());
        return ApiError.builder()
                .description(errMessage.toString())
                .code(HttpStatus.BAD_REQUEST.value()).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleRequestBodyIsMissedException(HttpMessageNotReadableException exception) {
        String errMessage = "Input fields are null";

        log.warn("{}\n {}", errMessage, exception.getMessage());
        return ApiError.builder()
                .description(errMessage)
                .code(HttpStatus.BAD_REQUEST.value()).build();
    }

//    @ExceptionHandler
//    public ApiError getDockerHandler(HttpHostConnectException exception) {
//        String errMessage = "Internal server exception";
//        System.out.println(exception.getHost());
//        System.out.println(exception.getHost().getHostName());
//        System.out.println(exception.getHost().getPort());
//        log.warn("{}:\n {}", errMessage, exception.getMessage());
//        return ApiError.builder()
//                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .description(errMessage)
//                .build();
//    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError getUnknownErrorHandler(Exception exception) {
        String errMessage = "Internal server exception";
        log.warn("{}:\n {}", errMessage, exception.getMessage());
        return ApiError.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .description(errMessage)
                .build();
    }
}
