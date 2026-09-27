package http.server.queue.consumer;

import http.server.queue.model.CodeResult;
import http.server.queue.model.Task;
import http.server.queue.model.enums.Status;
import http.server.queue.model.queue.ResultMessage;
import http.server.queue.service.CodeRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@EnableRabbit
@RequiredArgsConstructor
public class CodeProcessor {

    private final CodeRunner runner;

    private final RabbitTemplate rabbitTemplate;

    @Transactional
    @RabbitListener(queues = "${broker.queue}")
    public void processTaskFromQueue(Task task) {
        log.debug("task by id {} in process", task.getId());

        try {
            CompletableFuture<CodeResult> taskResult = runner.execute(task);

            CodeResult result = taskResult.get();

            task.setStatus(Status.Ready);
            log.debug("Task completed by id {}", task.getId());

            rabbitTemplate.convertAndSend("result.queue", new ResultMessage(result, task.getId()));
        } catch (ExecutionException e) {
            task.setStatus(Status.Failed);

            log.error("Task execution failed {}", task.getId(), e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            task.setStatus(Status.Failed);

            log.error("Task {} processing was interrupted", task.getId());
        }
    }
}
