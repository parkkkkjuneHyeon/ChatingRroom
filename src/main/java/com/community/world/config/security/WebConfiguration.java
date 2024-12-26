package com.community.world.config.security;

import com.community.world.config.jwt.JwtAtuthenticationExceptionFilter;
import com.community.world.config.jwt.JwtAuthenticationFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAtuthenticationExceptionFilter jwtAtuthenticationExceptionFilter;
    public WebConfiguration(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAtuthenticationExceptionFilter jwtAtuthenticationExceptionFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAtuthenticationExceptionFilter = jwtAtuthenticationExceptionFilter;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/login",
                                "/api/login",
                                "/signup",
                                "/home",
                                "/ws/info",
                                "/ws/**",
                                "/api/oauth/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(CsrfConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAtuthenticationExceptionFilter,
                        jwtAuthenticationFilter.getClass())
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled",havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers("/favicon.ico");
    }

}
