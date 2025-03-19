package http.server.backend.exceptions.Task;

public abstract class TaskException extends RuntimeException {
    public TaskException(String message) {
        super(message);
    }
}
