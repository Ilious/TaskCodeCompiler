package http.server.queue.exception;

public class CodeExecutionException extends RuntimeException {

    public CodeExecutionException(Throwable cause) {
        super(cause);
    }

    public CodeExecutionException(String message) {
        super(message);
    }
}
