package http.server.backend.service.interfaces;


import http.server.dto.CodeResultDto;
import http.server.dto.TaskDto;
import http.server.dto.enums.Status;

import java.util.UUID;

public interface ITaskService {
    TaskDto postTask(String code, String compiler, Long userId);

    TaskDto getTaskById(UUID id, Long userId);

    Status getStatusByTaskId(UUID id, Long userId);

    CodeResultDto getResultByTaskId(UUID id, Long userId);

}
