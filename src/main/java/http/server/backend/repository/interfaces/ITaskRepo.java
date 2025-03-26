package http.server.backend.repository.interfaces;

import http.server.backend.model.Task;

public interface ITaskRepo {

    Task postTask(String id, Task task);

    Task putTask(String id, Task task);

    Task getTask(String id);

    Task deleteTask(String id);
}
