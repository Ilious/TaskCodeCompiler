package http.server.backend.service.auth;

import http.server.backend.mappers.SessionMapper;
import http.server.backend.model.session.Session;
import http.server.backend.model.session.SessionDto;
import http.server.backend.model.user.UserDto;
import http.server.backend.repository.SessionRepoJPA;
import http.server.backend.service.interfaces.ISessionService;
import http.server.backend.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SessionService implements ISessionService {

    private final SessionMapper sessionMapper;

    private final SessionRepoJPA sessionRepo;

    private final IUserService userService;

    @Override
    public SessionDto createSession(UserDto userDto) {
        Session session = sessionMapper.toEntity(userDto);

        session.setUser(userService.getUserById(userDto.id()));

        return sessionMapper.toDto(sessionRepo.save(session));
    }

    @Override
    public boolean validateSession(String sessionId) {
        Optional<Session> session = sessionRepo.findById(sessionId);

        if (session.isEmpty())
            return false;

        Session sessionObj = session.get();

        if (Instant.now().isAfter(session.get().getStTime().plus(sessionObj.getDuration()))){
            removeSession(sessionId);
            return false;
        }

        return true;
    }

    @Override
    public void removeSession(String sessionId) {
        sessionRepo.removeById(sessionId);
    }

    @Override
    public Long getUserId(String token) {
        Optional<Session> session = sessionRepo.findById(token);
        return session
                .map(value -> value.getUser().getId())
                .orElse(null);
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    @Override
    public void clearExpiredSessions() {
        Set<String> expiredSessions = new HashSet<>();

        for (Session session : sessionRepo.findAll())
            if (Instant.now().isAfter(session.getStTime().plus(session.getDuration())))
                expiredSessions.add(session.getId());

        for (String sessionId : expiredSessions)
            removeSession(sessionId);
    }
}
