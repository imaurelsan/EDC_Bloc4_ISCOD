package com.gamesUP.gamesUP.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gamesUP.gamesUP.model.Game;

public interface GameRepository extends JpaRepository<Game, Integer> {

    List<Game> findByNomContainingIgnoreCase(String nom);
}
