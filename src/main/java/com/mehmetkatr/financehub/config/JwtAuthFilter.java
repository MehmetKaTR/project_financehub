package com.mehmetkatr.financehub.config;

import com.mehmetkatr.financehub.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        // 1. İstekten Authorization header'ını al
        String authHeader = request.getHeader("Authorization");

        // 2. Header yoksa VEYA "Bearer " ile başlamıyorsa → token yok, devam et
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. "Bearer " kısmını at, sadece token'ı al (7 karakter = "Bearer ")
        String token = authHeader.substring(7);

        // 4. Token geçerliyse kullanıcıyı Spring'e "tanıt"
        if (tokenService.validateToken(token)) {

            String email = tokenService.getEmailFromToken(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            Collections.emptyList()
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 5. İsteği devam ettir (turnikeyi aç)
        filterChain.doFilter(request, response);
    }

}
