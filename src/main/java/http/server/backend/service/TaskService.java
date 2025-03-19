package http.server.backend.service;


import http.server.backend.exceptions.Task.TaskCreateException;
import http.server.backend.model.Task;
import http.server.backend.model.enums.Status;
import http.server.backend.repository.TaskRepo;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static java.lang.Thread.sleep;

@Service
public class TaskService {

    private final TaskRepo taskRepo;

    private String generateIdx() {
        return UUID.randomUUID().toString();
    }

    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public Task postTask(String code, String compiler) {
        Task task = new Task(generateIdx(), code, compiler, Status.InProgress);

        try {
            sleep(Duration.of(3, ChronoUnit.SECONDS));
        } catch (InterruptedException e) {
            throw new TaskCreateException(task.getId());
        }

        return taskRepo.postTask(task.getId(), task);
    }

    public Task getTaskById(String id) {
        return taskRepo.getTask(id);
    }

    public Status getStatusByTaskId(String id) {
        return getTaskById(id).getStatus();
    }

    public String getResultByTaskId(String id) {
        Task taskById = getTaskById(id);
        return String.format("%s %s", taskById.getCompiler(), taskById.getCode());
    }
}
