package http.server.backend.sender;

import http.server.backend.config.rabbit.RabbitConfig;
import http.server.backend.model.Task;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitService implements IRabbitService {

    private final RabbitTemplate rabbitTemplate;

    private final String KEY;

    private final String EXCHANGE;

    public RabbitService(RabbitTemplate rabbitTemplate, RabbitConfig config) {
        this.rabbitTemplate = rabbitTemplate;
        KEY = config.getKey();
        EXCHANGE = config.getExchange();
    }

    @Override
    public void sendMessage(Task task) {
        rabbitTemplate.convertAndSend(EXCHANGE, KEY, task.getId());
    }
}
