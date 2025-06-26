package http.server.backend.exceptions.storage;

import lombok.Getter;

@Getter
public abstract class EntityException extends RuntimeException {

    private final String model;

    private final String value;

    public EntityException(String message, String model, String value) {
        super(message.formatted(model, value));
        this.model = model;
        this.value = value;
    }
}
