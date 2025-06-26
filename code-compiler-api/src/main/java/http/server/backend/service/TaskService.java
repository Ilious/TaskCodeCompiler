package http.server.backend.service;


import http.server.backend.sender.IRabbitService;
import http.server.backend.service.interfaces.ITaskService;
import http.server.backend.model.CodeResult;
import http.server.backend.model.Task;
import http.server.backend.model.enums.Compiler;
import http.server.backend.model.enums.Status;
import http.server.backend.repository.interfaces.ICodeResultRepo;
import http.server.backend.repository.interfaces.ITaskRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

    private final ITaskRepo taskRepo;

    private final ICodeResultRepo codeResultRepo;

    private final IRabbitService rabbitService;

    private String generateIdx() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Task postTask(String code, String compiler) {
        Task task = new Task(generateIdx(), code, Compiler.from(compiler), Status.InProgress);

        Task sentTask = taskRepo.postTask(task.getId(), task);
        rabbitService.sendMessage(sentTask);

        return sentTask;
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
    public CodeResult getResultByTaskId(String id) {
        Task taskById = getTaskById(id);
        return codeResultRepo.getResult(id);
    }

    @Override
    public CodeResult putResultByTaskId(String id, CodeResult result) {
        return codeResultRepo.putResult(id, result);
    }
}
