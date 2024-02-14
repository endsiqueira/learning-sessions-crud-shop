package com.exs.learningsessionscrudshop.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String SECRET_KEY = "yourSecretKey"; // Substitua pela sua chave secreta real

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(new JWTTokenValidatorFilter(), UsernamePasswordAuthenticationFilter.class) // Alterado aqui
                .authorizeRequests()
                .anyRequest().authenticated();
        return http.build();
    }

    public static class JWTTokenValidatorFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
            String tokenHeader = request.getHeader("Authorization");
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                String token = tokenHeader.substring(7);
                try {
                    Jws<Claims> claimsJws = Jwts.parser()
                            .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                            .parseClaimsJws(token);

                    Claims claims = claimsJws.getBody();
                    String username = claims.getSubject();
                    List<String> authorities = claims.get("authorities", List.class);

                    if (username != null) {
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                } catch (Exception e) {
                    SecurityContextHolder.clearContext();
                }
            }

            filterChain.doFilter(request, response);
        }
    }
}
