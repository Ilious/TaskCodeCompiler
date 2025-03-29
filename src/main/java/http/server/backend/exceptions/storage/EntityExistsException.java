package http.server.backend.exceptions.storage;

public class EntityExistsException extends EntityException {

    public EntityExistsException(String id, String model) {
        super("%s %s exists exception".formatted(model, id));
    }
}
