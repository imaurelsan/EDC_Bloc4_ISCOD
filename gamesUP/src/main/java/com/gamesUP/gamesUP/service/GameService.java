package com.gamesUP.gamesUP.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.dto.GameDTO;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final RecommandationClient recommandationClient;

    public GameService(GameRepository gameRepository, RecommandationClient recommandationClient) {
        this.gameRepository = gameRepository;
        this.recommandationClient = recommandationClient;
    }

    public List<Game> getAllJeux() {
        return gameRepository.findAll();
    }

    public GameDTO versDTO(Game game) {
        return new GameDTO(game.id, game.nom, game.prix, game.description, game.auteur, game.genre,
                game.category, game.publisher, game.numEdition);
    }

    public Game versEntite(GameDTO gameDTO) {
        Game game = new Game();
        game.id = gameDTO.getId();
        game.nom = gameDTO.getNom();
        game.prix = gameDTO.getPrix();
        game.description = gameDTO.getDescription();
        game.auteur = gameDTO.getAuteur();
        game.genre = gameDTO.getGenre();
        game.category = gameDTO.getCategory();
        game.publisher = gameDTO.getPublisher();
        game.numEdition = gameDTO.getNumEdition();
        return game;
    }

    public Game ajouterJeu(Game game) {
        return gameRepository.save(game);
    }

    public Game findById(int id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new JeuNonTrouveException(id));
    }

    public void deleteById(int id) {
        gameRepository.deleteById(id);
    }

    public List<Game> rechercherParNom(String nom) {
        return gameRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Game> recommander(int idUtilisateur) {
        List<Game> jeux = new java.util.ArrayList<>();
        for (Integer idJeu : recommandationClient.recommander(idUtilisateur)) {
            try {
                jeux.add(findById(idJeu));
            } catch (JeuNonTrouveException exception) {
                // Ignore les jeux qui n'existent plus.
            }
        }
        return jeux;
    }

    public Game modifierJeu(int id, Game game) {
        gameRepository.findById(id)
                .orElseThrow(() -> new JeuNonTrouveException(id));
        game.id = id;
        return gameRepository.save(game);
    }

    public void supprimerJeu(int id) {
        if (!gameRepository.existsById(id)) {
            throw new JeuNonTrouveException(id);
        }
        gameRepository.deleteById(id);
    }
}
