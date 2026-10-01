package http.server.queue.consumer;

import http.server.queue.exception.CodeExecutionException;
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

@Slf4j
@Component
@EnableRabbit
@RequiredArgsConstructor
public class CodeProcessor {

    private final CodeRunner runner;

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "${broker.queue}")
    public void processTaskFromQueue(Task task) {
        log.debug("task by id {} in process", task.getId());

        try {
            CodeResult result = runner.execute(task);

            task.setStatus(Status.READY);
            log.debug("Task completed by id {}", task.getId());

            rabbitTemplate.convertAndSend("result.queue", new ResultMessage(result, task.getId()));
        } catch (CodeExecutionException e) {
            task.setStatus(Status.FAILED);
            rabbitTemplate.convertAndSend(
                    "result.queue",
                    new ResultMessage(new CodeResult("", "", Status.FAILED), task.getId())
            );

            log.error("Task execution failed {} {}", task.getId(), e.getMessage());
        }
    }
}
