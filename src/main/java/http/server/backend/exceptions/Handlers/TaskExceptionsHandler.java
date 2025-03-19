package http.server.backend.exceptions.Handlers;


import http.server.backend.exceptions.Task.TaskFoundException;
import http.server.backend.exceptions.Task.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TaskExceptionsHandler {

    @ExceptionHandler(TaskFoundException.class)
    public ResponseEntity<String> getTaskFoundHandler(TaskFoundException taskFoundException) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(taskFoundException.getMessage());
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> getTaskNotFoundHandler(TaskNotFoundException taskFoundException) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(taskFoundException.getMessage());
    }
}
