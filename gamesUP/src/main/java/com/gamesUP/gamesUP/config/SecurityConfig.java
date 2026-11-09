package com.gamesUP.gamesUP.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager users() {
        var client = User.withUsername("client")
                .password("{noop}client")
                .roles("CLIENT")
                .build();
        var admin = User.withUsername("admin")
                .password("{noop}admin")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(client, admin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/jeux/**").hasAnyRole("CLIENT", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/jeux/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/jeux/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/jeux/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/utilisateurs/me").hasAnyRole("CLIENT", "ADMIN")
                .requestMatchers("/utilisateurs/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/commandes/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/commandes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/commandes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/commandes/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
