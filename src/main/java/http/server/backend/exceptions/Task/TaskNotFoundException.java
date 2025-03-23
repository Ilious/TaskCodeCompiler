package http.server.backend.exceptions.Task;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String id) {
        super("storage doesn't contain key %s".formatted(id));
    }
}
