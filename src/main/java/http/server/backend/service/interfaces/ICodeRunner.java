package http.server.backend.service.interfaces;

import http.server.backend.model.CodeResult;
import http.server.backend.model.Task;

import java.util.concurrent.CompletableFuture;

public interface ICodeRunner {

    CompletableFuture<CodeResult> execute(Task task);
}
