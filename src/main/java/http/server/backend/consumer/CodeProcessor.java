package http.server.backend.consumer;

import http.server.backend.model.CodeResult;
import http.server.backend.model.Task;
import http.server.backend.model.enums.Status;
import http.server.backend.repository.interfaces.ICodeResultRepo;
import http.server.backend.repository.interfaces.ITaskRepo;
import http.server.backend.service.CodeRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@EnableRabbit
@RequiredArgsConstructor
public class CodeProcessor {

    private final ITaskRepo taskRepo;

    private final ICodeResultRepo codeResultRepo;

    private final CodeRunner runner;

    @RabbitListener(queues = "${broker.queue}")
    public void processTaskFromQueue(String id) throws ExecutionException, InterruptedException {
        Task task = taskRepo.getTask(id);

        CompletableFuture<CodeResult> taskResult = runner.execute(task);
        log.debug("Process task from queue by id: {}", taskResult.get());
        task.setStatus(Status.Ready);

        codeResultRepo.putResult(id, taskResult.get());
    }
}
