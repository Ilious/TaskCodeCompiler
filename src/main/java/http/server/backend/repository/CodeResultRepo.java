package http.server.backend.repository;

import http.server.backend.exceptions.storage.EntityExistsException;
import http.server.backend.exceptions.storage.EntityNotFoundException;
import http.server.backend.model.CodeResult;
import http.server.backend.repository.interfaces.ICodeResultRepo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CodeResultRepo implements ICodeResultRepo {

    private final Map<String, CodeResult> storage = new HashMap<>();

    @Override
    public CodeResult putResult(String id, CodeResult result) {
        storage.put(id, result);

        return result;
    }

    @Override
    public CodeResult getResult(String id) throws EntityNotFoundException {
        if (!storage.containsKey(id))
            throw new EntityNotFoundException(id, "CodeResult");

        return storage.get(id);
    }
}
