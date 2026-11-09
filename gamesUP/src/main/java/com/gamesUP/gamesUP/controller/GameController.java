package com.gamesUP.gamesUP.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.service.GameService;

@RestController
@RequestMapping("/jeux")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<Game> getAllJeux() {
        return gameService.getAllJeux();
    }

    @GetMapping("/recherche")
    public List<Game> rechercherParNom(@RequestParam String nom) {
        return gameService.rechercherParNom(nom);
    }

    @PostMapping
    public ResponseEntity<Game> ajouterJeu(@RequestBody Game game) {
        return ResponseEntity.status(201).body(gameService.ajouterJeu(game));
    }

    @PutMapping("/{id}")
    public Game modifierJeu(@PathVariable int id, @RequestBody Game game) {
        return gameService.modifierJeu(id, game);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerJeu(@PathVariable int id) {
        gameService.supprimerJeu(id);
        return ResponseEntity.noContent().build();
    }
}