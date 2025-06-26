package http.server.backend.sender;

import http.server.backend.config.rabbit.RabbitConfig;
import http.server.backend.model.CodeResult;
import http.server.backend.model.Task;
import http.server.backend.model.enums.Status;
import http.server.backend.model.queue.ResultMessage;
import http.server.backend.repository.TaskRepo;
import http.server.backend.repository.interfaces.ICodeResultRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class RabbitService implements IRabbitService {

    private final TaskRepo taskRepo;
    Map<String, CompletableFuture<CodeResult>> codeResultStorage = new ConcurrentHashMap<>();

    private final RabbitTemplate rabbitTemplate;

    private final ICodeResultRepo codeResultRepo;

    private final String KEY;

    private final String EXCHANGE;

    public RabbitService(ICodeResultRepo codeResultRepo, RabbitTemplate rabbitTemplate, RabbitConfig config, TaskRepo taskRepo) {
        this.rabbitTemplate = rabbitTemplate;
        KEY = config.getKey();
        EXCHANGE = config.getExchange();
        this.codeResultRepo = codeResultRepo;
        this.taskRepo = taskRepo;
    }

    @Override
    public void sendMessage(Task task) {
        log.debug("task with id {} sent", task.getId());

        codeResultStorage.put(task.getId(), new CompletableFuture<>());

        rabbitTemplate.convertAndSend(EXCHANGE, KEY, task);
    }

    @RabbitListener(queues = "result.queue")
    public void processResult(ResultMessage result) {
        String taskId = result.getCorrelationId();
        log.debug("received result for task: {}", taskId);

        CompletableFuture<CodeResult> futureResult = codeResultStorage.remove(taskId);
        if (futureResult != null) {
            taskRepo.getTask(taskId).setStatus(Status.Ready);
            codeResultRepo.putResult(taskId, result.getResult());
        }
    }
}
