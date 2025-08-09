package http.server.backend.repository.interfaces;

import http.server.backend.exceptions.storage.EntityNotFoundException;
import http.server.backend.model.CodeResult;

public interface ICodeResultRepo {

    CodeResult putResult(String id, CodeResult result);

    CodeResult getResult(String id) throws EntityNotFoundException;
}

