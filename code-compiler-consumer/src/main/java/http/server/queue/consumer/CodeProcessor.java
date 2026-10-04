package http.server.queue.consumer;

import http.server.dto.CodeResultDto;
import http.server.dto.ResultMessage;
import http.server.dto.TaskDto;
import http.server.dto.enums.Status;
import http.server.queue.exception.CodeExecutionException;
import http.server.queue.exception.CodeExecutionTimeoutException;
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
    public void processTaskFromQueue(TaskDto task) {
        log.debug("task by id {} in process", task.getId());

        try {
            CodeResultDto result = runner.execute(task);

            log.debug("Task completed by id {}", task.getId());

            rabbitTemplate.convertAndSend("result.queue", new ResultMessage(result, task.getId()));
        } catch (CodeExecutionTimeoutException e) {
            sendFailureResult(task, Status.TIME_OUT, e);

            log.error("Task execution failed according to TIME OUT {} {}", task.getId(), e.getMessage());
        } catch (CodeExecutionException e) {
            sendFailureResult(task, Status.FAILED, e);

            log.error("Task execution failed {} {}", task.getId(), e.getMessage());
        }
    }

    private void sendFailureResult(TaskDto task, Status status, Exception e) {
        CodeResultDto result = CodeResultDto.builder()
                .codeResult("")
                .codeError(e.getMessage())
                .status(status)
                .exitCode(null)
                .build();

        rabbitTemplate.convertAndSend(
                "result.queue",
                new ResultMessage(result, task.getId())
        );
    }
}
