package http.server.queue.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import http.server.dto.CodeResultDto;
import http.server.dto.TaskDto;
import http.server.dto.enums.Compiler;
import http.server.dto.enums.Status;
import http.server.queue.component.codeExecutor.CExecutor;
import http.server.queue.component.codeExecutor.CPlusesExecutor;
import http.server.queue.component.codeExecutor.CodeExecutor;
import http.server.queue.component.codeExecutor.PythonExecutor;
import http.server.queue.config.docker.DockerCompilerConfig;
import http.server.queue.exception.CodeExecutionException;
import http.server.queue.exception.CodeExecutionTimeoutException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is a service to run tasks in c++ / c / python
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CodeRunner {

    private static final String TAG = "compiler";
    private final DockerClient client;
    private final PythonExecutor pythonExecutor;
    private final CPlusesExecutor cPlusesExecutor;
    private final CExecutor cExecutor;
    private final DockerCompilerConfig dockerCompilerConfig;

    private String dockerFilePath;

    private Long codeRunTimeout;

    private Long maxOutputBytes;

    /**
     * This method returns special DockerFile with all built-in programming languages
     */
    private File getDockerFile() {
        return Path.of(dockerFilePath, "Dockerfile")
                .toAbsolutePath()
                .toFile();
    }

    /**
     * This method builds image from Dockerfile in dockerCompiler dir
     */
    @PostConstruct
    public void buildImg() {
        this.dockerFilePath = dockerCompilerConfig.getPath();
        this.codeRunTimeout = dockerCompilerConfig.getTimeoutInSeconds();
        this.maxOutputBytes = dockerCompilerConfig.getMaxOutputBytes();
        Long codeBuiltTimeout = dockerCompilerConfig.getImageBuiltTimeout();

        try {
            boolean built = client.buildImageCmd()
                    .withTags(Set.of(TAG))
                    .withDockerfile(getDockerFile())
                    .exec(new ResultCallback.Adapter<>())
                    .awaitCompletion(codeBuiltTimeout, TimeUnit.SECONDS);

            if (!built)
                throw new IllegalStateException(
                        "Docker compiler image build timed out"
                );

            log.info("Image {} built successfully", TAG);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                    "Compiler image build interrupted", e
            );

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to build compiler image", e
            );
        }
    }

    /**
     * Executes a task using the executor for its compiler.
     *
     * @param task object of a task to be run in dockerClient
     * @return {@link CodeResultDto}
     */
    public CodeResultDto execute(TaskDto task) {
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
     * Then returns result from logs in CodeResultDto and removes container in the end.
     *
     * @param code     the string lines of code
     * @param executor the CodeExecutor defined in execute method
     * @return {@link  CodeResultDto}
     */
    private CodeResultDto run(String code, CodeExecutor executor) throws CodeExecutionException {
        String containerId = null;
        final StringBuilder builderOut = new StringBuilder();
        final StringBuilder builderErr = new StringBuilder();
        try {
            containerId = startContainer(client, executor.getCmdParams(code));
            log.debug("Container started: {}", containerId);

            getCodeResultInBuilder(client, containerId, builderOut, false);
            getCodeResultInBuilder(client, containerId, builderErr, true);

            Long exitCodeLong = getExitCode(containerId);

            return new CodeResultDto(
                    builderOut.toString(),
                    builderErr.toString(),
                    Status.READY,
                    exitCodeLong
            );
        } catch (InterruptedException exception) {
            Thread thread = Thread.currentThread();
            thread.interrupt();

            log.warn("Thread {} was interrupted", thread.getName(), exception);

            throw new CodeExecutionException(exception);
        } catch (CodeExecutionException e) {
            throw e;
        } catch (Exception exception) {
            log.error("Code execution failed", exception);

            throw new CodeExecutionException(exception);
        } finally {
            if (containerId != null)
                try {
                    removeContainer(containerId, client);
                } catch (Exception cleanUpException) {
                    log.warn("Failed to remove container {}",
                            containerId,
                            cleanUpException);
                }
        }
    }

    private Long getExitCode(String containerId) {
        InspectContainerResponse inspectContainer = client.inspectContainerCmd(containerId).exec();
        return inspectContainer.getState().getExitCodeLong();
    }

    /**
     * This is a method to get logs from container after running a task
     *
     * @param client      the DockerClient to interact with docker
     * @param containerId the id of a container on pc
     * @param logResult   the StringBuilder to collect logs
     * @param isStdErr    takes out without errs or with err
     */
    private void getCodeResultInBuilder(DockerClient client, String containerId, StringBuilder logResult,
                                        boolean isStdErr) throws InterruptedException {
        AtomicInteger byteCounter = new AtomicInteger();

        client.logContainerCmd(containerId)
                .withStdOut(!isStdErr)
                .withStdErr(isStdErr)
                .withFollowStream(true)
                .exec(new ResultCallback.Adapter<>() {
                    @Override
                    public void onNext(Frame object) {
                        byte[] payload = object.getPayload();

                        int total = byteCounter.addAndGet(payload.length);

                        if (total > maxOutputBytes)
                            throw new CodeExecutionException("Output exceeded %d bytes".formatted(maxOutputBytes));

                        logResult.append(new String(object.getPayload(), StandardCharsets.UTF_8));
                        super.onNext(object);
                    }
                }).awaitCompletion(10, TimeUnit.SECONDS);
    }

    /**
     * This is a method to run process in container. It's a main method before getLogs from container
     *
     * @param client    the DockerClient to interact with docker
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
     *
     * @param client      the DockerClient to interact with docker
     * @param containerId containerId to be removed if it exists
     */
    private void removeContainer(String containerId, DockerClient client) {
        if (containerId != null) {
            client.removeContainerCmd(containerId).withForce(true).exec();
        }
    }
}
