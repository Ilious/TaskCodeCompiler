package http.server.backend.repository;

import http.server.backend.exceptions.Task.TaskFoundException;
import http.server.backend.exceptions.Task.TaskNotFoundException;
import http.server.backend.model.Task;
import http.server.backend.repository.interfaces.ITaskRepo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TaskRepo implements ITaskRepo {

    private final Map<String, Task> storage = new HashMap<>();

    @Override
    public Task putTask(String id, Task task) {
        storage.put(id, task);

        return task;
    }

    @Override
    public Task postTask(String id, Task task) {
        if (storage.get(id) != null)
            throw new TaskFoundException(id);

        storage.put(id, task);
        return task;
    }

    @Override
    public Task deleteTask(String id) {
        if (!storage.containsKey(id))
            throw new TaskNotFoundException(id);

        return storage.remove(id);
    }

    @Override
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
