package http.server.backend.sender;

import http.server.backend.config.rabbit.RabbitConfig;
import http.server.backend.service.CodeResultService;
import http.server.dto.ResultMessage;
import http.server.dto.TaskDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitService implements IRabbitService {

    private final RabbitTemplate rabbitTemplate;

    private final String KEY;

    private final String EXCHANGE;

    private final CodeResultService codeResultService;

    public RabbitService(RabbitTemplate rabbitTemplate, RabbitConfig config, CodeResultService codeResultService) {
        this.rabbitTemplate = rabbitTemplate;
        KEY = config.getKey();
        EXCHANGE = config.getExchange();
        this.codeResultService = codeResultService;
    }

    @Override
    public void sendMessage(TaskDto task) {
        log.debug("task with id {} sent", task.getId());

        rabbitTemplate.convertAndSend(EXCHANGE, KEY, task);
    }

    @RabbitListener(queues = "result.queue")
    public void processResult(ResultMessage message) {
        log.debug("Message with task {} received", message.getCorrelationId());

        codeResultService.saveTaskResult(message);
    }
}
