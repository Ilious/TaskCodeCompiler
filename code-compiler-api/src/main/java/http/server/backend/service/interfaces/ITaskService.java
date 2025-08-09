package http.server.backend.service.interfaces;


import http.server.backend.model.CodeResult;
import http.server.backend.model.Task;
import http.server.backend.model.enums.Status;

public interface ITaskService {
    Task postTask(String code, String compiler);

    Task getTaskById(String id);

    Status getStatusByTaskId(String id);

    CodeResult getResultByTaskId(String id);

    CodeResult putResultByTaskId(String id, CodeResult result);
}
