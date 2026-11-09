package com.gamesUP.gamesUP.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repository.GameRepository;

public class GameController {

    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GetMapping
    public List<Game> getAllJeux() {
        return gameRepository.findAll();
    }

    @PostMapping
    public void ajouterJeu(@RequestBody Game game) {
        gameRepository.save(game);
    }
}