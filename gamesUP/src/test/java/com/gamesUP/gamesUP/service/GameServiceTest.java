package com.gamesUP.gamesUP.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.gamesUP.gamesUP.dto.GameDTO;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private RecommandationClient recommandationClient;

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
        when(gameRepository.existsById(1)).thenReturn(true);

        gameService.supprimerJeu(1);

        verify(gameRepository).deleteById(1);
    }

    @Test
    void findByIdLeveUneExceptionSiLeJeuEstAbsent() {
        when(gameRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(JeuNonTrouveException.class, () -> gameService.findById(1));
    }

    @Test
    void supprimerJeuLeveUneExceptionSiIdInconnu() {
        when(gameRepository.existsById(1)).thenReturn(false);

        assertThrows(JeuNonTrouveException.class, () -> gameService.supprimerJeu(1));
    }

    @Test
    void recommanderRenvoieLesJeuxRecommandes() {
        Game game = new Game();
        when(recommandationClient.recommander(1)).thenReturn(List.of(1));
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        List<Game> resultat = gameService.recommander(1);

        assertSame(game, resultat.get(0));
        verify(recommandationClient).recommander(1);
        verify(gameRepository).findById(1);
    }

    @Test
    void recommanderIgnoreUnJeuInexistant() {
        when(recommandationClient.recommander(1)).thenReturn(List.of(99));
        when(gameRepository.findById(99)).thenReturn(Optional.empty());

        List<Game> resultat = gameService.recommander(1);

        org.junit.jupiter.api.Assertions.assertTrue(resultat.isEmpty());
        verify(gameRepository).findById(99);
    }

    @Test
    void versDTORenvoieLesInformationsDuJeu() {
        Game game = new Game();
        game.id = 1;
        game.nom = "Catan";
        game.prix = 39.99;
        game.description = "Jeu de stratégie";

        GameDTO resultat = gameService.versDTO(game);

        assertSame("Catan", resultat.getNom());
        org.junit.jupiter.api.Assertions.assertEquals(1, resultat.getId());
        org.junit.jupiter.api.Assertions.assertEquals(39.99, resultat.getPrix());
        assertSame("Jeu de stratégie", resultat.getDescription());
    }

    @Test
    void versEntiteRenvoieLesInformationsDuDTO() {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setId(1);
        gameDTO.setNom("Catan");

        Game resultat = gameService.versEntite(gameDTO);

        assertSame("Catan", resultat.nom);
        org.junit.jupiter.api.Assertions.assertEquals(1, resultat.id);
    }
}
