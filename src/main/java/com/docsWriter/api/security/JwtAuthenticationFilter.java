package com.docsWriter.api.security;

import com.docsWriter.api.database.entities.AccountEntity;
import com.docsWriter.api.database.repositories.AccountRepository;
import com.docsWriter.api.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AccountRepository accountRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            UUID accountId = jwtUtil.verifyAccessTokenAndGetUserId(token);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                AccountEntity account = accountRepository.findById(accountId)
                        .orElse(null);

                if (account != null && account.isActive()) {
                    var authentication = new UsernamePasswordAuthenticationToken(
                            account,
                            null,
                            Collections.emptyList()
                    );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (RuntimeException e) {
            log.debug("JWT verification failed: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
