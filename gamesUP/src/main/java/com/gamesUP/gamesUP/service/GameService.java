package com.gamesUP.gamesUP.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repository.GameRepository;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllJeux() {
        return gameRepository.findAll();
    }

    public Game ajouterJeu(Game game) {
        return gameRepository.save(game);
    }

    public Game findById(int id) {
        return gameRepository.findById(id).orElseThrow();
    }

    public void deleteById(int id) {
        gameRepository.deleteById(id);
    }

    public List<Game> rechercherParNom(String nom) {
        return gameRepository.findByNomContainingIgnoreCase(nom);
    }

    public Game modifierJeu(int id, Game game) {
        gameRepository.findById(id).orElseThrow();
        game.id = id;
        return gameRepository.save(game);
    }

    public void supprimerJeu(int id) {
        gameRepository.deleteById(id);
    }
}
