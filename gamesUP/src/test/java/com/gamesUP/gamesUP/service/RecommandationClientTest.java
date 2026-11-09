package com.gamesUP.gamesUP.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class RecommandationClientTest {

    @Mock
    private RestTemplate restTemplate;

    private RecommandationClient recommandationClient;

    @BeforeEach
    void preparerClient() {
        recommandationClient = new RecommandationClient(restTemplate);
    }

    @Test
    void appelReussiRenvoieLesIdentifiants() {
        when(restTemplate.getForObject(
                "http://localhost:8000/recommandations/1", Integer[].class))
                .thenReturn(new Integer[] { 2, 3 });

        List<Integer> resultat = recommandationClient.recommander(1);

        assertEquals(List.of(2, 3), resultat);
    }

    @Test
    void exceptionRenvoieUneListeVide() {
        when(restTemplate.getForObject(
                "http://localhost:8000/recommandations/1", Integer[].class))
                .thenThrow(new RestClientException("API indisponible"));

        List<Integer> resultat = recommandationClient.recommander(1);

        assertEquals(List.of(), resultat);
    }
}
