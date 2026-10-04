package http.server.backend.service;


import http.server.backend.exceptions.authentication.AuthenticationException;
import http.server.backend.exceptions.storage.EntityNotFoundException;
import http.server.backend.mappers.CodeResultMapper;
import http.server.backend.mappers.TaskMapper;
import http.server.backend.model.codeResult.CodeResult;
import http.server.backend.model.task.Task;
import http.server.backend.repository.CodeResultRepoJPA;
import http.server.backend.repository.TaskRepoJPA;
import http.server.backend.sender.IRabbitService;
import http.server.backend.service.interfaces.ITaskService;
import http.server.backend.service.interfaces.IUserService;
import http.server.dto.CodeResultDto;
import http.server.dto.TaskDto;
import http.server.dto.enums.Compiler;
import http.server.dto.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

    private final TaskRepoJPA taskRepo;

    private final CodeResultRepoJPA codeResultRepo;

    private final IRabbitService rabbitService;

    private final IUserService userService;

    private final TaskMapper taskMapper;

    private final CodeResultMapper codeResultMapper;

    @Override
    @Transactional
    public TaskDto postTask(String code, String compiler, Long userId) {
        Task task = Task.builder()
                .code(code)
                .compiler(Compiler.from(compiler))
                .status(Status.IN_PROGRESS)
                .user(userService.getUserById(userId))
                .build();

        Task saved = taskRepo.save(task);
        TaskDto dto = taskMapper.toDto(saved);
        rabbitService.sendMessage(dto);

        return dto;
    }

    @Override
    public TaskDto getTaskById(UUID id, Long userId) {
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString(), "task"));

        if (!task.getUser().getId().equals(userId))
            throw new AuthenticationException(
                    "User is not authorized for the task %s".formatted(id),
                    HttpStatus.UNAUTHORIZED
            );

        return taskMapper.toDto(task);
    }

    @Override
    public Status getStatusByTaskId(UUID id, Long userId) {
        return getTaskById(id, userId).getStatus();
    }

    @Override
    public CodeResultDto getResultByTaskId(UUID id, Long userId) {
        log.debug("started get");
        TaskDto taskById = getTaskById(id, userId);
        CodeResult codeResult = codeResultRepo.findByTaskId(id);

        CodeResultDto dto = codeResultMapper.toDto(codeResult);
        log.debug("{}", dto);
        return dto;
    }
    }
