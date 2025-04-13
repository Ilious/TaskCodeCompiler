package http.server.backend.exceptions.storage;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends EntityException {

    private final String model;

    private final String value;

    public EntityNotFoundException(String value, String model) {
        super("%s storage doesn't contain key %s".formatted(model, value));
        this.value = value;
        this.model = model;
    }
}
