package http.server.backend.exceptions.storage;

public abstract class EntityException extends RuntimeException {
    public EntityException(String message) {
        super(message);
    }
}
