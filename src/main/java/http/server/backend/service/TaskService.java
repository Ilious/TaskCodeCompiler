package http.server.backend.service;


import http.server.backend.exceptions.storage.EntityCreateException;
import http.server.backend.model.Task;
import http.server.backend.model.enums.Status;
import http.server.backend.repository.interfaces.ITaskRepo;
import http.server.backend.service.interfaces.ITaskService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static java.lang.Thread.sleep;

@Service
public class TaskService implements ITaskService {

    private final ITaskRepo taskRepo;

    private String generateIdx() {
        return UUID.randomUUID().toString();
    }

    public TaskService(ITaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    @Override
    public Task postTask(String code, String compiler) throws EntityCreateException {
        Task task = new Task(generateIdx(), code, compiler, Status.InProgress);

        try {
            sleep(Duration.of(3, ChronoUnit.SECONDS));
        } catch (InterruptedException e) {
            throw new EntityCreateException(task.getId(), "task");
        }

        return taskRepo.postTask(task.getId(), task);
    }

    @Override
    public Task getTaskById(String id) {
        return taskRepo.getTask(id);
    }

    @Override
    public Status getStatusByTaskId(String id) {
        return getTaskById(id).getStatus();
    }

    @Override
    public String getResultByTaskId(String id) {
        Task taskById = getTaskById(id);
        return String.format("%s %s", taskById.getCompiler(), taskById.getCode());
    }
}
