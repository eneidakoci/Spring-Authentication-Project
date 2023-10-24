package com.joan.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity @EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final UsernameAndPasswordAuthFilter usernameAndPasswordAuthFilter;
    private final JwtFilter jwtFilter;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.exceptionHandling(e -> e.authenticationEntryPoint(userAuthenticationEntryPoint));

        http
                .addFilterBefore(usernameAndPasswordAuthFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernameAndPasswordAuthFilter.class);

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(CorsConfigurer::disable);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(requests -> {
            requests.requestMatchers(HttpMethod.POST, "/v1/auth/log-in", "/v1/auth/sign-up").permitAll()
              //      .requestMatchers("/v1/hello/john").hasAnyAuthority("ROLE_EDITOR")
                    .anyRequest().authenticated();
        });
        return http.build();
    }
}
