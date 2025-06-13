package http.server.backend.repository;

import http.server.backend.exceptions.storage.EntityExistsException;
import http.server.backend.exceptions.storage.EntityNotFoundException;
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
    public Task postTask(String id, Task task) throws EntityExistsException {
        if (storage.get(id) != null)
            throw new EntityExistsException(id, "task");

        storage.put(id, task);
        return task;
    }

    @Override
    public Task deleteTask(String id) throws EntityNotFoundException {
        if (!storage.containsKey(id))
            throw new EntityNotFoundException(id, "task");

        return storage.remove(id);
    }

    @Override
    public Task getTask(String id) throws EntityNotFoundException {
        if (!storage.containsKey(id))
            throw new EntityNotFoundException(id, "task");

        return storage.get(id);
    }
}
