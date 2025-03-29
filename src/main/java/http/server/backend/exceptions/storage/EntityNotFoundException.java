package http.server.backend.exceptions.storage;

public class EntityNotFoundException extends EntityException {
    public EntityNotFoundException(String value, String model) {
        super("%s storage doesn't contain key %s".formatted(model, value));
    }
}
