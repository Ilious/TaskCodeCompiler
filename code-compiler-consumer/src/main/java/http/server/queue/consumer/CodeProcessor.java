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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@EnableRabbit
@RequiredArgsConstructor
public class CodeProcessor {

    private final CodeRunner runner;

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "${broker.queue}")
    public void processTaskFromQueue(Task task) throws ExecutionException, InterruptedException {
        log.debug("task by id {} in process", task.getId());

        CompletableFuture<CodeResult> taskResult = runner.execute(task);
        log.debug("Proceed task from queue by id: {}", task.getId());
        task.setStatus(Status.Ready);

        rabbitTemplate.convertAndSend("result.queue", new ResultMessage(taskResult.get(), task.getId()));
    }
}
