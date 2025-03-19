package http.server.backend.exceptions.Task;

public class TaskCreateException extends TaskException {
    public TaskCreateException(String id) {
        super("Error creating task id: %s".formatted(id));
    }
}
