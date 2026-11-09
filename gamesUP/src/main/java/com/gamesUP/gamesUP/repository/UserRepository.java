package com.gamesUP.gamesUP.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gamesUP.gamesUP.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByNom(String nom);
}
