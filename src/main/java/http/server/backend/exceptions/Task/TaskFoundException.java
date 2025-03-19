package http.server.backend.exceptions.Task;

public class TaskFoundException extends TaskException {

    public TaskFoundException(String id) {
        super("Task %s exists exception".formatted(id));
    }
}
