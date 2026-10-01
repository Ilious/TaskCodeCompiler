package http.server.queue.exception;

public class CodeExecutionTimeoutException extends CodeExecutionException {

    public CodeExecutionTimeoutException(String msg) {
        super(msg);
    }
}
