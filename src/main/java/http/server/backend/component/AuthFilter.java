package http.server.backend.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import http.server.backend.exceptions.authentication.AuthenticationException;
import http.server.backend.model.api.ApiError;
import http.server.backend.service.auth.SessionService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class AuthFilter implements Filter {

    private final static String AUTHORIZATION = "Authorization";

    private final SessionService sessionService;

    public AuthFilter(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public boolean matchesSwaggerPath(String requestURI) {
        return requestURI.startsWith("/v3/api-docs") ||
                requestURI.startsWith("/swagger-ui");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;

        String requestURI = httpReq.getRequestURI();
        if (requestURI.equals("/login") || requestURI.equals("/register") || matchesSwaggerPath(requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        try {
            final String token = getTokenFromRequest(httpReq);
            if (sessionService.validateSession(token)) {
                Long userId = sessionService.getUserId(token);

                if (userId != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userId,
                                    null,
                                    Collections.singletonList(new SimpleGrantedAuthority("USER"))
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                throw new AuthenticationException("Token is not correct", token);
            }
        } catch (AuthenticationException ex) {
            String err = String.format("Token [%s] is not correct", ex.getBearerToken());
            log.warn("{}", err);
            ApiError error = ApiError.builder()
                    .code(HttpStatus.FORBIDDEN.value())
                    .description(err)
                    .build();
            returnErr((HttpServletResponse) servletResponse, error);
        }
    }

    public String getTokenFromRequest(HttpServletRequest servletRequest) {
        final String bearer = servletRequest.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer "))
            return bearer.substring(7);
        throw new AuthenticationException("Token is not correct", null);
    }

    public void returnErr(HttpServletResponse resp, ApiError apiError) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        resp.setStatus(apiError.getCode());
        resp.getOutputStream().print(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(apiError));
    }
}
