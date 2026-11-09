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

    // Crée les deux utilisateurs directement en mémoire pour les tests.
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

    // Configure les règles de sécurité de l'API.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                // La lecture des jeux est autorisée pour le client et l'administrateur.
                .requestMatchers(HttpMethod.GET, "/jeux/**").hasAnyRole("CLIENT", "ADMIN")
                // L'ajout d'un jeu est réservé à l'administrateur.
                .requestMatchers(HttpMethod.POST, "/jeux/**").hasRole("ADMIN")
                // La modification d'un jeu est réservée à l'administrateur.
                .requestMatchers(HttpMethod.PUT, "/jeux/**").hasRole("ADMIN")
                // La suppression d'un jeu est réservée à l'administrateur.
                .requestMatchers(HttpMethod.DELETE, "/jeux/**").hasRole("ADMIN")
                // Toutes les autres routes demandent une authentification.
                .anyRequest().authenticated()
        )
        // L'API utilise HTTP Basic pour recevoir les identifiants dans la requête.
        .httpBasic(Customizer.withDefaults())
        // Il n'y a pas de formulaire HTML, donc la protection CSRF est désactivée pour l'API REST.
        .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
