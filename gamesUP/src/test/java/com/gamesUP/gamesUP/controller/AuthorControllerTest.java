package com.gamesUP.gamesUP.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.gamesUP.gamesUP.config.SecurityConfig;
import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.service.AuthorService;

@WebMvcTest(AuthorController.class)
@Import(SecurityConfig.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllRenvoie200() throws Exception {
        when(authorService.getAll()).thenReturn(List.of(new Author()));

        mockMvc.perform(get("/auteurs")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findByIdRenvoie200() throws Exception {
        when(authorService.findById(1L)).thenReturn(new Author());

        mockMvc.perform(get("/auteurs/1")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void enregistrerRenvoie201() throws Exception {
        when(authorService.versEntite(any())).thenReturn(new Author());
        when(authorService.enregistrer(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/auteurs").contentType("application/json").content("{\"name\":\"Tolkien\"}"))
                .andExpect(status().isCreated());

        verify(authorService).enregistrer(any(Author.class));
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void clientNePeutPasEnregistrerUnAuteur() throws Exception {
        mockMvc.perform(post("/auteurs")
                .contentType("application/json")
                .content("{\"name\":\"Tolkien\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void modifierRenvoie200() throws Exception {
        when(authorService.versEntite(any())).thenReturn(new Author());
        when(authorService.modifier(org.mockito.ArgumentMatchers.eq(1L), any(Author.class)))
                .thenAnswer(invocation -> invocation.getArgument(1));

        mockMvc.perform(put("/auteurs/1").contentType("application/json").content("{\"name\":\"Tolkien\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void supprimerRenvoie204() throws Exception {
        doNothing().when(authorService).supprimer(1L);

        mockMvc.perform(delete("/auteurs/1")).andExpect(status().isNoContent());
        verify(authorService).supprimer(1L);
    }
}
