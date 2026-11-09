package com.gamesUP.gamesUP.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gamesUP.gamesUP.model.Game;

public interface GameRepository extends JpaRepository<Game, Integer> {
}
