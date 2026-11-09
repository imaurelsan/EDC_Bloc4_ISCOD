package com.gamesUP.gamesUP.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager users() {
        // Utilisateurs en mémoire pour cet exercice.
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
                // Lecture des jeux : clients et admins
                .requestMatchers(HttpMethod.GET, "/jeux/**").hasAnyRole("CLIENT", "ADMIN")
                // Modification des jeux : admin seulement
                .requestMatchers(HttpMethod.POST, "/jeux/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/jeux/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/jeux/**").hasRole("ADMIN")
                // Lecture du profil : clients et admins
                .requestMatchers(HttpMethod.GET, "/utilisateurs/me").hasAnyRole("CLIENT", "ADMIN")
                // Gestion des comptes : admin seulement
                .requestMatchers("/utilisateurs/**").hasRole("ADMIN")
                // Lecture des commandes : utilisateur connecté
                .requestMatchers(HttpMethod.GET, "/commandes/**").authenticated()
                // Modification des commandes : admin seulement
                .requestMatchers(HttpMethod.POST, "/commandes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/commandes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/commandes/**").hasRole("ADMIN")
                // Autres routes : utilisateur connecté
                .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults())
        // API sans session, appelée en JSON
        .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
