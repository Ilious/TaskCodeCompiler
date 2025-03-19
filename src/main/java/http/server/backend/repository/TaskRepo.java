package http.server.backend.repository;

import http.server.backend.exceptions.Task.TaskFoundException;
import http.server.backend.exceptions.Task.TaskNotFoundException;
import http.server.backend.model.Task;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TaskRepo {

    private final Map<String, Task> storage = new HashMap<>();

    public Task putTask(String id, Task task) {
        storage.put(id, task);

        return task;
    }

    public Task postTask(String id, Task task) {
        if (storage.get(id) != null)
            throw new TaskFoundException(id);

        storage.put(id, task);
        return task;
    }

    public Task deleteTask(String id) {
        if (!storage.containsKey(id))
            throw new TaskNotFoundException(id);

        return storage.remove(id);
    }

    public Task getTask(String id) {
        if (!storage.containsKey(id))
            throw new TaskNotFoundException(id);

        Task task = storage.get(id);
        return Task.builder()
                .id(task.getId())
                .status(task.getStatus())
                .code(task.getCode())
                .compiler(task.getCompiler()).build();
    }
}
