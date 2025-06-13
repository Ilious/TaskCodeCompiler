package http.server.backend.sender;

import http.server.backend.model.Task;

public interface IRabbitService {

    void sendMessage(Task task);
}
