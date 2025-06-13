package http.server.backend.repository.interfaces;

import http.server.backend.exceptions.storage.EntityExistsException;
import http.server.backend.exceptions.storage.EntityNotFoundException;
import http.server.backend.model.Task;

public interface  ITaskRepo {

    Task postTask(String id, Task task) throws EntityExistsException;

    Task putTask(String id, Task task);

    Task getTask(String id) throws EntityNotFoundException;

    Task deleteTask(String id) throws EntityNotFoundException;
}
