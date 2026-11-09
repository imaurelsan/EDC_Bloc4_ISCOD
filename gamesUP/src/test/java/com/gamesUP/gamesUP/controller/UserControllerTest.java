package com.gamesUP.gamesUP.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.gamesUP.gamesUP.config.SecurityConfig;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.service.UserService;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "client", roles = "CLIENT")
    void clientPeutVoirSonCompte() throws Exception {
        when(userService.findByNom("client")).thenReturn(new User());

        mockMvc.perform(get("/utilisateurs/me"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "client", roles = "CLIENT")
    void clientNePeutPasCreerUnCompte() throws Exception {
        mockMvc.perform(post("/utilisateurs")
                .contentType("application/json")
                .content("{\"nom\":\"nouveau\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void adminPeutCreerUnCompte() throws Exception {
        when(userService.enregistrer(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/utilisateurs")
                .contentType("application/json")
                .content("{\"nom\":\"nouveau\"}"))
                .andExpect(status().isCreated());

        verify(userService).enregistrer(any(User.class));
    }

    @Test
    @WithMockUser(username = "client", roles = "CLIENT")
    void clientNePeutPasSupprimerUnCompte() throws Exception {
        mockMvc.perform(delete("/utilisateurs/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void adminPeutSupprimerUnCompte() throws Exception {
        doNothing().when(userService).supprimer(1);

        mockMvc.perform(delete("/utilisateurs/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "client", roles = "CLIENT")
    void clientNePeutPasLireLaListeDesComptes() throws Exception {
        mockMvc.perform(get("/utilisateurs"))
                .andExpect(status().isForbidden());
    }
}
