package http.server.queue.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import http.server.queue.component.codeExecutor.CExecutor;
import http.server.queue.component.codeExecutor.CPlusesExecutor;
import http.server.queue.component.codeExecutor.CodeExecutor;
import http.server.queue.component.codeExecutor.PythonExecutor;
import http.server.queue.exception.CodeExecutionException;
import http.server.queue.exception.CodeExecutionTimeoutException;
import http.server.queue.model.CodeResult;
import http.server.queue.model.Task;
import http.server.queue.model.enums.Compiler;
import http.server.queue.model.enums.Status;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * This is a service to run tasks in c++ / c / python
 */
@Slf4j
@Service
@Profile("!test")
@RequiredArgsConstructor
public class CodeRunner {

    private final DockerClient client;

    private final PythonExecutor pythonExecutor;

    private final CPlusesExecutor cPlusesExecutor;

    private final CExecutor cExecutor;

    private static final String TAG = "compiler";

    @Value("${app.docker.compiler.path}")
    private String dockerFilePath;

    @Value("${app.docker.compiler.timeout-in-seconds:30}")
    private Long codeRunTimeout;

    /**
     * This method returns special DockerFile with all built-in programming languages
     */
    private File getDockerFile() {
        return dockerFilePath.contains("app") ?
                new File(dockerFilePath) :
                Path.of(
                Paths.get(".").toAbsolutePath().toString(),
                dockerFilePath,
               "Dockerfile"
        ).toFile();
    }

    /**
     * This method builds image from Dockerfile in dockerCompiler dir
     */
    @PostConstruct
    public void buildImg() {
        try {
            client.buildImageCmd()
                    .withTags(Set.of(TAG))
                    .withDockerfile(getDockerFile())
                    .exec(new ResultCallback.Adapter<>())
                    .awaitCompletion(codeRunTimeout, TimeUnit.SECONDS);
            log.info("Image for codeRunner built");

            client.tagImageCmd("debian", TAG, "latest");
        } catch (Exception ex) {
            log.error("Unexpected err in buildImg() [CodeRunner]: {}", ex.getMessage());
        }
    }

    /**
     * This is an async method to define code language or throw InternalServerException
     * @param task object of a task to be run in dockerClient
     * @return CompletableFuture<CodeResult>
     */
    public CodeResult execute(Task task) {
        return switch (task.getCompiler()) {
            case Compiler.C -> run(task.getCode(), cExecutor);
            case Compiler.CPluses -> run(task.getCode(), cPlusesExecutor);
            case Compiler.Py -> run(task.getCode(), pythonExecutor);
        };
    }

    /**
     * This is an async method to run code from Task by CodeExecutor defined in execute.
     * It gets dockerClient from DockerStarterConfig and runs methods
     * pullImageIfNotExist -> startContainer -> getCodeResultInBuilder.
     * Then returns result from logs in CompletableFuture and removes container in the end.
     * @param code the string lines of code
     * @param executor the CodeExecutor defined in execute method
     * @return CompletableFuture<CodeResult>
     */
    private CodeResult run(String code, CodeExecutor executor) throws CodeExecutionException {
        String containerId = null;
        final StringBuilder builderOut = new StringBuilder();
        final StringBuilder builderErr = new StringBuilder();
        try {
            containerId = startContainer(client, executor.getCmdParams(code));
            log.debug("Container started: {}", containerId);

            getCodeResultInBuilder(client, containerId, builderOut, false);
            getCodeResultInBuilder(client, containerId, builderErr, true);

            return new CodeResult(
                    builderOut.toString().trim(),
                    builderErr.toString().trim(),
                    Status.READY
            );
        } catch (InterruptedException exception) {
            Thread thread = Thread.currentThread();
            thread.interrupt();

            log.warn("Thread {} was interrupted", thread.getName(), exception);

            throw new CodeExecutionException(exception);
        } catch (Exception exception) {
            log.error("Code execution failed {}", exception.getMessage());

            throw new CodeExecutionException(exception);
        }
        finally {
            removeContainer(containerId, client);
        }
    }

    /**
     * This is a method to get logs from container after running a task
     * @param client the DockerClient to interact with docker
     * @param containerId the id of a container on pc
     * @param logResult the StringBuilder to collect logs
     * @param isStdErr takes out without errs or with err
     */
    private void getCodeResultInBuilder(DockerClient client, String containerId, StringBuilder logResult,
                                        boolean isStdErr) throws InterruptedException {
        client.logContainerCmd(containerId)
                .withStdOut(!isStdErr)
                .withStdErr(isStdErr)
                .withFollowStream(true)
                .exec(new ResultCallback.Adapter<>() {
                    @Override
                    public void onNext(Frame object) {
                        logResult.append(new String(object.getPayload(), StandardCharsets.UTF_8));
                        super.onNext(object);
                    }
                }).awaitCompletion(10, TimeUnit.SECONDS);

        if (logResult.length() >= 2000) {
            log.warn("Exception getting result: too long result boundary >= 2000 characters, {}", logResult.length());
            throw new CodeExecutionException("Exception getting result: too long result boundary >= 2000 characters");
        }
    }

    /**
     * This is a method to run process in container. It's a main method before getLogs from container
     * @param client the DockerClient to interact with docker
     * @param cmdParams the params to run code in container
     */
    private String startContainer(DockerClient client, String[] cmdParams)
             throws InterruptedException {
        String containerId = client
                .createContainerCmd(TAG)
                .withHostConfig(HostConfig.newHostConfig()
                        .withMemory(512 * 1024 * 1024L)
                        .withCpuCount(1L)
                )
                .withCmd(cmdParams)
                .withNetworkDisabled(true)
                .exec().getId();

        client.startContainerCmd(containerId).exec();

        boolean isFinished = client.waitContainerCmd(containerId)
                .exec(new WaitContainerResultCallback())
                .awaitCompletion(codeRunTimeout, TimeUnit.SECONDS);

        if (!isFinished)
            throw new CodeExecutionTimeoutException(
                    String.format("Execution exceeded %d seconds", codeRunTimeout)
            );

        return containerId;
    }

    /**
     * It removes container
     * @param client the DockerClient to interact with docker
     * @param containerId containerId to be removed if it exists
     */
    private void removeContainer(String containerId, DockerClient client) {
        if (containerId != null) {
            client.removeContainerCmd(containerId).withForce(true).exec();
        }
    }
}
