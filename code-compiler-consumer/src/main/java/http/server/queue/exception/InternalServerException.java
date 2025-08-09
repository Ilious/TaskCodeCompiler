package http.server.queue.exception;

public class InternalServerException extends RuntimeException {

    public InternalServerException() {
        super("Unexpected err has occurred on the server");
    }

    public InternalServerException(String msg) {
        super("Unexpected err has occurred on the server %s".formatted(msg));
    }
}
