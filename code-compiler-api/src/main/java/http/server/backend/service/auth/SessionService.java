package http.server.backend.service.auth;

import http.server.backend.config.SessionConfig;
import http.server.backend.mappers.SessionMapper;
import http.server.backend.model.session.Session;
import http.server.backend.model.session.SessionDto;
import http.server.backend.model.user.ResponseUser;
import http.server.backend.model.user.User;
import http.server.backend.repository.SessionRepoJPA;
import http.server.backend.service.interfaces.ISessionService;
import http.server.backend.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionService implements ISessionService {

    private final SessionMapper sessionMapper;

    private final SessionRepoJPA sessionRepo;

    private final IUserService userService;

    private final SessionConfig config;

    @Override
    public SessionDto createSession(ResponseUser userDto) {
        User userById = userService.getUserById(userDto.id());

        Instant now = Instant.now();
        Session session = Session.builder()
                .user(userById)
                .createdAt(now)
                .expiresAt(now.plus(Duration.ofMinutes(config.getTtlMinutes())))
                .build();

        return sessionMapper.toDto(sessionRepo.save(session));
    }

    @Override
    public boolean validateSession(UUID sessionId) {
        return sessionRepo.findById(sessionId)
                .map(session -> !session.isExpired())
                .orElse(false);
    }

    @Override
    public void removeSession(UUID sessionId) {
        sessionRepo.removeById(sessionId);
    }

    @Override
    public Long getUserId(UUID token) {
        Optional<Session> session = sessionRepo.findById(token);
        return session
                .map(value -> value.getUser().getId())
                .orElse(null);
    }

    @Transactional
    @Scheduled(
            fixedRateString = "${app.session.clean-interval-hours:1}",
            timeUnit = TimeUnit.HOURS
    )
    public void clearExpiredSessions() {
        Instant now = Instant.now();
        int deleted = sessionRepo.deleteExpired(now);

        log.info("Expired sessions are cleared {}", deleted);
    }
}
