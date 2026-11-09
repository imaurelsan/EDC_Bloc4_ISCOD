package com.gamesUP.gamesUP.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.gamesUP.gamesUP.config.SecurityConfig;
import com.gamesUP.gamesUP.dto.GameDTO;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.service.GameService;
import com.gamesUP.gamesUP.service.JeuNonTrouveException;

@WebMvcTest(GameController.class)
@Import(SecurityConfig.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @BeforeEach
    void preparerLesConversions() {
        when(gameService.versEntite(org.mockito.ArgumentMatchers.any(GameDTO.class)))
                .thenAnswer(invocation -> {
                    GameDTO dto = invocation.getArgument(0);
                    Game game = new Game();
                    game.nom = dto.getNom();
                    return game;
                });
        when(gameService.versDTO(org.mockito.ArgumentMatchers.any(Game.class)))
                .thenAnswer(invocation -> {
                    Game game = invocation.getArgument(0);
                    GameDTO dto = new GameDTO();
                    dto.setNom(game.nom);
                    return dto;
                });
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void getJeuxAvecClientRenvoie200() throws Exception {
        when(gameService.getAllJeux()).thenReturn(List.of(new Game()));

        mockMvc.perform(get("/jeux"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void ajouterJeuAvecClientRenvoie403() throws Exception {
        mockMvc.perform(post("/jeux")
                .contentType("application/json")
                .content("{\"nom\":\"Catan\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getJeuxSansAuthentificationRenvoie401() throws Exception {
        mockMvc.perform(get("/jeux"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void rechercherJeuAvecClientRenvoie200() throws Exception {
        when(gameService.rechercherParNom("cat")).thenReturn(List.of(new Game()));

        mockMvc.perform(get("/jeux/recherche").param("nom", "cat"))
                .andExpect(status().isOk());

        verify(gameService).rechercherParNom("cat");
    }

    @Test
    void rechercherJeuSansAuthentificationRenvoie401() throws Exception {
        mockMvc.perform(get("/jeux/recherche").param("nom", "cat"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void modifierJeuAvecAdminRenvoie200() throws Exception {
        when(gameService.modifierJeu(org.mockito.ArgumentMatchers.eq(1),
                org.mockito.ArgumentMatchers.any(Game.class)))
                .thenAnswer(invocation -> invocation.getArgument(1));

        mockMvc.perform(put("/jeux/1")
                .contentType("application/json")
                .content("{\"nom\":\"Catan\"}"))
                .andExpect(status().isOk());

        verify(gameService).modifierJeu(org.mockito.ArgumentMatchers.eq(1),
                org.mockito.ArgumentMatchers.any(Game.class));
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void modifierJeuAvecClientRenvoie403() throws Exception {
        mockMvc.perform(put("/jeux/1")
                .contentType("application/json")
                .content("{\"nom\":\"Catan\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void supprimerJeuAvecAdminRenvoie204() throws Exception {
        mockMvc.perform(delete("/jeux/1"))
                .andExpect(status().isNoContent());

        verify(gameService).supprimerJeu(1);
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void supprimerJeuAvecClientRenvoie403() throws Exception {
        mockMvc.perform(delete("/jeux/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void ajouterJeuAvecAdminRenvoie201AvecNom() throws Exception {
        when(gameService.ajouterJeu(org.mockito.ArgumentMatchers.any(Game.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/jeux")
                .contentType("application/json")
                .content("{\"nom\":\"Catan\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Catan"));

        verify(gameService).ajouterJeu(org.mockito.ArgumentMatchers.any(Game.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void modifierJeuAvecIdInconnuRenvoie404() throws Exception {
        when(gameService.modifierJeu(org.mockito.ArgumentMatchers.eq(1),
                org.mockito.ArgumentMatchers.any(Game.class)))
                .thenThrow(new JeuNonTrouveException(1));

        mockMvc.perform(put("/jeux/1")
                .contentType("application/json")
                .content("{\"nom\":\"Catan\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void recommanderAvecClientRenvoie200() throws Exception {
        when(gameService.recommander(1)).thenReturn(List.of(new Game()));

        mockMvc.perform(get("/jeux/recommandations/1"))
                .andExpect(status().isOk());

        verify(gameService).recommander(1);
    }
}