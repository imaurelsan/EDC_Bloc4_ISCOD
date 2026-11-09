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
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.service.PublisherService;

@WebMvcTest(PublisherController.class)
@Import(SecurityConfig.class)
class PublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherService publisherService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllRenvoie200() throws Exception {
        when(publisherService.getAll()).thenReturn(List.of(new Publisher()));

        mockMvc.perform(get("/editeurs")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findByIdRenvoie200() throws Exception {
        when(publisherService.findById(1L)).thenReturn(new Publisher());

        mockMvc.perform(get("/editeurs/1")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void enregistrerRenvoie201() throws Exception {
        when(publisherService.versEntite(any())).thenReturn(new Publisher());
        when(publisherService.enregistrer(any(Publisher.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/editeurs").contentType("application/json").content("{\"name\":\"Asmodee\"}"))
                .andExpect(status().isCreated());

        verify(publisherService).enregistrer(any(Publisher.class));
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void clientNePeutPasEnregistrerUnEditeur() throws Exception {
        mockMvc.perform(post("/editeurs")
                .contentType("application/json")
                .content("{\"name\":\"Asmodee\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void modifierRenvoie200() throws Exception {
        when(publisherService.versEntite(any())).thenReturn(new Publisher());
        when(publisherService.modifier(org.mockito.ArgumentMatchers.eq(1L), any(Publisher.class)))
                .thenAnswer(invocation -> invocation.getArgument(1));

        mockMvc.perform(put("/editeurs/1").contentType("application/json").content("{\"name\":\"Asmodee\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void supprimerRenvoie204() throws Exception {
        doNothing().when(publisherService).supprimer(1L);

        mockMvc.perform(delete("/editeurs/1")).andExpect(status().isNoContent());
        verify(publisherService).supprimer(1L);
    }
}
