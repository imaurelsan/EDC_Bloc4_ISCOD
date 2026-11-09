package com.gamesUP.gamesUP.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repository.GameRepository;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    void findByIdRenvoieLeJeuQuandIlExiste() {
        Game game = new Game();
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        Game resultat = gameService.findById(1);

        assertSame(game, resultat);
    }

    @Test
    void deleteByIdAppelleLeRepository() {
        gameService.deleteById(1);

        verify(gameRepository).deleteById(1);
    }

    @Test
    void rechercherParNomRenvoieLaListeDuRepository() {
        List<Game> jeux = List.of(new Game());
        when(gameRepository.findByNomContainingIgnoreCase("cat")).thenReturn(jeux);

        List<Game> resultat = gameService.rechercherParNom("cat");

        assertSame(jeux, resultat);
    }

    @Test
    void getAllJeuxRenvoieLaListeDuRepository() {
        List<Game> jeux = List.of(new Game());
        when(gameRepository.findAll()).thenReturn(jeux);

        List<Game> resultat = gameService.getAllJeux();

        assertSame(jeux, resultat);
        verify(gameRepository).findAll();
    }

    @Test
    void ajouterJeuAppelleSaveEtRenvoieLeJeuSauvegarde() {
        Game game = new Game();
        when(gameRepository.save(game)).thenReturn(game);

        Game resultat = gameService.ajouterJeu(game);

        assertSame(game, resultat);
        verify(gameRepository).save(game);
    }

    @Test
    void modifierJeuSauvegardeLeJeuAvecLeBonId() {
        Game game = new Game();
        when(gameRepository.findById(1)).thenReturn(Optional.of(new Game()));
        when(gameRepository.save(game)).thenReturn(game);

        Game resultat = gameService.modifierJeu(1, game);

        assertSame(game, resultat);
        org.junit.jupiter.api.Assertions.assertEquals(1, game.id);
        verify(gameRepository).findById(1);
        verify(gameRepository).save(game);
    }

    @Test
    void supprimerJeuAppelleDeleteById() {
        gameService.supprimerJeu(1);

        verify(gameRepository).deleteById(1);
    }
}
