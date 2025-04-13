package http.server.backend.exceptions.storage;

import lombok.Getter;

@Getter
public class EntityExistsException extends EntityException {

    private final String id;

    private final String model;

    public EntityExistsException(String id, String model) {
        super("%s %s exists exception".formatted(model, id));
        this.id = id;
        this.model = model;
    }
}
