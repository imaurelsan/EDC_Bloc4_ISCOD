package com.gamesUP.gamesUP.integration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.net.HttpURLConnection;
import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gamesUP.gamesUP.service.RecommandationClient;

@SpringBootTest
class RecommandationClientIntegrationTest {

    @Autowired
    private RecommandationClient recommandationClient;

    @BeforeEach
    void verifierApiPython() {
        boolean apiDisponible = false;
        try {
            HttpURLConnection connexion = (HttpURLConnection) URI
                    .create("http://localhost:8000/")
                    .toURL()
                    .openConnection();
            connexion.setConnectTimeout(1000);
            connexion.setReadTimeout(1000);
            connexion.setRequestMethod("GET");
            apiDisponible = connexion.getResponseCode() == 200;
            connexion.disconnect();
        } catch (Exception exception) {
            apiDisponible = false;
        }

        assumeTrue(apiDisponible, "API Python indisponible");
    }

    @Test
    void envoyerInteractionContacteApiPython() {
        assertDoesNotThrow(() -> recommandationClient.envoyerInteraction(1, 2));
    }

    @Test
    void recommanderRenvoieUneReponse() {
        assertNotNull(recommandationClient.recommander(1));
    }
}
