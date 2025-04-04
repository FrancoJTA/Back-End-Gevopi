package com.example.gevopi_back_end.Security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final byte[] secretKeyBytes;

    public JwtAuthenticationFilter(@Value("${jwt.secret}") String secretKey) {
        if (secretKey == null || secretKey.length() < 32) {
            throw new IllegalArgumentException("La clave JWT debe tener al menos 32 caracteres");
        }

        this.secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        logger.info("Clave JWT configurada ({} bytes)", secretKeyBytes.length);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        // quitar la condicion graphql
        // path.startsWith("/graphql")

        System.out.println("HOLA");

        if (path.startsWith("/graphiql") || path.startsWith("/graphql") || path.startsWith("/graphql/tests")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String token = getJwtFromRequest(request);

            if (!StringUtils.hasText(token)) {
                logger.warn("No se proporcionó token JWT");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token requerido");
                return;
            }

            debugToken(token);

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKeyBytes) // Usar los bytes directamente
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Autenticación exitosa
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            claims.getSubject() != null ? claims.getSubject() : "anonymous",
                            null,
                            new ArrayList<>());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("Autenticación exitosa para: {}", claims.getSubject());

        } catch (ExpiredJwtException e) {
            logger.error("Token expirado: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expirado");
            return;
        } catch (JwtException e) {
            logger.error("Error de validación JWT: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void debugToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                logger.debug("Token Header: {}", new String(java.util.Base64.getUrlDecoder().decode(parts[0])));
                logger.debug("Token Payload: {}", new String(java.util.Base64.getUrlDecoder().decode(parts[1])));
            }
        } catch (Exception e) {
            logger.warn("Error al decodificar token para debug", e);
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}