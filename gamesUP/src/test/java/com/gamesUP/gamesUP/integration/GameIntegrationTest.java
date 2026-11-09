package com.gamesUP.gamesUP.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.gamesUP.gamesUP.repository.GameRepository;

@SpringBootTest
@AutoConfigureMockMvc
class GameIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameRepository gameRepository;

    @AfterEach
    void nettoyerLesJeux() {
        gameRepository.deleteAll();
    }

    @Test
    void ajouterPuisRechercherUnJeu() throws Exception {
        mockMvc.perform(post("/jeux")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nom\":\"Catan\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/jeux/recherche")
                .with(user("client").roles("CLIENT"))
                .param("nom", "cat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nom", is("Catan")));
    }
}
