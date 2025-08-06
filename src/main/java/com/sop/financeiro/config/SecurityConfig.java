package com.sop.financeiro.config;

import com.sop.financeiro.service.AuthService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@SecurityScheme(name = SecurityConfig.SECURITY, type = SecuritySchemeType.HTTP, bearerFormat = "JWT",scheme = "bearer")
public class SecurityConfig {

        private final AuthService authService;
        private final JwtFilter jwtFilter;
        public static final String SECURITY = "bearerAuth";

        public SecurityConfig(AuthService authService, JwtFilter jwtFilter) {
        this.authService = authService;
            this.jwtFilter = jwtFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                    "/api/auth/**",
                                    "/swagger-ui/**",
                                    "/v3/api-docs/**",
                                    "/swagger-ui.html",
                                    "/swagger-resources/**",
                                    "/webjars/**",
                                    "/configuration/**",
                                    "/error",
                                    "/docs/**"
                            ).permitAll()
                            .anyRequest().authenticated()
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
        }

          @Bean
          public PasswordEncoder passwordEncoder() {
             return new BCryptPasswordEncoder();
          }

          @Bean
          public AuthenticationProvider authenticationProvider() {
                    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                    authProvider.setUserDetailsService(authService);
                    authProvider.setPasswordEncoder(passwordEncoder());
                    return authProvider;
         }

         @Bean
         public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
             return config.getAuthenticationManager();
         }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOriginPatterns(List.of("*")); // <- permite qualquer origem
            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
            configuration.setAllowCredentials(true);
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }
}