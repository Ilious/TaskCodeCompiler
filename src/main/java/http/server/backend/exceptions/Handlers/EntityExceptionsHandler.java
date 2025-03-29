package http.server.backend.exceptions.Handlers;


import http.server.backend.exceptions.storage.EntityCreateException;
import http.server.backend.exceptions.storage.EntityExistsException;
import http.server.backend.exceptions.storage.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class EntityExceptionsHandler {

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<String> getEntityExistsHandler(EntityExistsException entityExistsException) {
        log.warn("EntityExistsException: {}", entityExistsException.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(entityExistsException.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> getEntityNotFoundHandler(EntityNotFoundException entityNotFoundException) {
        log.warn("EntityNotFoundException: {}", entityNotFoundException.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(entityNotFoundException.getMessage());
    }

    @ExceptionHandler(EntityCreateException.class)
    public ResponseEntity<String> getEntityCreateHandler(EntityCreateException entityFoundException) {
        log.warn("EntityCreateException: {}", entityFoundException.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(entityFoundException.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> getNullFieldsHandler(NullPointerException nullPointerException) {
        log.warn("NullFieldsException fields cannot be null: {}", nullPointerException.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(nullPointerException.getMessage());
    }
}
