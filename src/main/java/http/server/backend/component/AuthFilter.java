package http.server.backend.component;

import http.server.backend.service.auth.SessionService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;

@Component
public class AuthFilter implements Filter {

    private final String AUTHORIZATION = "Authorization";

    private final SessionService sessionService;

    public AuthFilter(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public boolean matchesSwaggerPath(String requestURI) {
        return  requestURI.startsWith("/v3/api-docs") ||
                requestURI.startsWith("/swagger-ui");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResp = (HttpServletResponse) servletResponse;

        String requestURI = httpReq.getRequestURI();
        if (requestURI.equals("/login") || requestURI.equals("/register") || matchesSwaggerPath(requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        final String token = getTokenFromRequest(httpReq);
        if (token != null && sessionService.validateSession(token)) {
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
            servletResponse.setContentType("application/json");
            servletResponse.setCharacterEncoding("UTF-8");
            String errorResp = "{\"error\":\"Unauthorized\",\"message\":\"Session is not valid\"}";
            servletResponse.getWriter().write(errorResp);
            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            servletResponse.flushBuffer();
        }
    }

    public String getTokenFromRequest(HttpServletRequest servletRequest) {
        final String bearer = servletRequest.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer "))
            return bearer.substring(7);
        return null;
    }
}
