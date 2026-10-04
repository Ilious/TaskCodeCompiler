package http.server.backend.service;

import http.server.backend.exceptions.storage.EntityNotFoundException;
import http.server.backend.model.codeResult.CodeResult;
import http.server.backend.model.task.Task;
import http.server.backend.repository.CodeResultRepoJPA;
import http.server.backend.repository.TaskRepoJPA;
import http.server.dto.CodeResultDto;
import http.server.dto.ResultMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeResultService {

    private final TaskRepoJPA taskRepoJPA;

    private final CodeResultRepoJPA codeResultRepo;

    @Transactional
    public void saveTaskResult(ResultMessage message) {
        UUID taskId = message.getCorrelationId();
        log.debug("received result for task: {}", taskId);
        log.debug("{}", message);

        Task taskById = taskRepoJPA.findByIdForUpdate(taskId)
                .orElseThrow(() -> new EntityNotFoundException(taskId, "Task"));

        if (taskById.getCodeResult() != null) {
            log.warn("Code result is already attached to the task {}", taskId);
            return;
        }

        CodeResultDto result = message.getResult();

        CodeResult built = CodeResult.builder()
                .task(taskById)
                .codeResult(result.getCodeResult())
                .codeError(result.getCodeError())
                .status(result.getStatus())
                .exitCode(result.getExitCode())
                .build();

        CodeResult saved = codeResultRepo.save(built);

        taskById.setStatus(result.getStatus());
        taskById.setCodeResult(saved);
    }
}
