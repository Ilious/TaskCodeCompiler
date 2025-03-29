package http.server.backend.exceptions.storage;

public class EntityCreateException extends EntityException {
    public EntityCreateException(String id, String model) {
        super("Error creating %s by: %s".formatted(model, id));
    }
}
