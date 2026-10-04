package http.server.backend.repository;

import http.server.backend.model.codeResult.CodeResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CodeResultRepoJPA extends JpaRepository<CodeResult, UUID> {

    CodeResult findByTaskId(UUID id);
}
