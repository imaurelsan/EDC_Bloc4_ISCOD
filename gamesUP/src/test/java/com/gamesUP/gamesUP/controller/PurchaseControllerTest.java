package com.gamesUP.gamesUP.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.gamesUP.gamesUP.config.SecurityConfig;
import com.gamesUP.gamesUP.service.PurchaseService;

@WebMvcTest(PurchaseController.class)
@Import(SecurityConfig.class)
class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseService purchaseService;

    @Test
    @WithMockUser(roles = "CLIENT")
    void clientAuthentifiePeutLireLesCommandes() throws Exception {
        mockMvc.perform(get("/commandes"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminPeutCreerUneCommande() throws Exception {
        mockMvc.perform(post("/commandes")
                .contentType("application/json")
                .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "client", roles = "CLIENT")
    void clientNePeutPasCreerUneCommande() throws Exception {
        mockMvc.perform(post("/commandes")
                .contentType("application/json")
                .content("{}"))
                .andExpect(status().isForbidden());
    }

}
