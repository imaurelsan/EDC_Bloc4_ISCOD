package com.gamesUP.gamesUP.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.dto.GameDTO;
import com.gamesUP.gamesUP.service.GameService;

@RestController
@RequestMapping("/jeux")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<GameDTO> getAllJeux() {
        return gameService.getAllJeux().stream().map(gameService::versDTO).toList();
    }

    @GetMapping("/recherche")
    public List<GameDTO> rechercherParNom(@RequestParam String nom) {
        return gameService.rechercherParNom(nom).stream().map(gameService::versDTO).toList();
    }

    @GetMapping("/recommandations/{idUtilisateur}")
    public List<GameDTO> recommander(@PathVariable int idUtilisateur) {
        return gameService.recommander(idUtilisateur).stream().map(gameService::versDTO).toList();
    }

    @GetMapping("/{id}")
    public GameDTO findById(@PathVariable int id) {
        return gameService.versDTO(gameService.findById(id));
    }

    @PostMapping
    public ResponseEntity<GameDTO> ajouterJeu(@RequestBody GameDTO gameDTO) {
        Game game = gameService.ajouterJeu(gameService.versEntite(gameDTO));
        return ResponseEntity.status(201).body(gameService.versDTO(game));
    }

    @PutMapping("/{id}")
    public GameDTO modifierJeu(@PathVariable int id, @RequestBody GameDTO gameDTO) {
        Game game = gameService.modifierJeu(id, gameService.versEntite(gameDTO));
        return gameService.versDTO(game);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerJeu(@PathVariable int id) {
        gameService.supprimerJeu(id);
        return ResponseEntity.noContent().build();
    }
}