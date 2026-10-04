package http.server.backend.exceptions.storage;

import java.util.UUID;

public class EntityNotFoundException extends EntityException {

    public EntityNotFoundException(String value, String model) {
        super("%s storage doesn't contain key %s", model, value);
    }

    public EntityNotFoundException(UUID value, String model) {
        this(value.toString(), model);
    }
}
