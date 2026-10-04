package http.server.backend.sender;


import http.server.dto.TaskDto;

public interface IRabbitService {

    void sendMessage(TaskDto task);
}
