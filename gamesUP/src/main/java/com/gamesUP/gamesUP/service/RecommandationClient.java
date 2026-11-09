package com.gamesUP.gamesUP.service;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RecommandationClient {

    private static final Logger logger = LoggerFactory.getLogger(RecommandationClient.class);
    private final RestTemplate restTemplate;

    @Value("${recommandation.url:http://localhost:8000}")
    private String url = "http://localhost:8000";

    public RecommandationClient() {
        this(new RestTemplate());
    }

    public RecommandationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Integer> recommander(int idUtilisateur) {
        try {
            Integer[] recommandations = restTemplate.getForObject(
                    url + "/recommandations/" + idUtilisateur, Integer[].class);
            return recommandations == null ? Collections.emptyList() : Arrays.asList(recommandations);
        } catch (RestClientException exception) {
            logger.error("Impossible de joindre l'API de recommandation", exception);
            return Collections.emptyList();
        }
    }

    public void envoyerInteraction(int idUtilisateur, int idJeu) {
        try {
            Map<String, Integer> interaction = Map.of(
                    "user_id", idUtilisateur,
                    "game_id", idJeu,
                    "interaction", 1);
            restTemplate.postForObject(url + "/interactions", interaction, Void.class);
        } catch (RestClientException exception) {
            logger.error("Impossible d'envoyer l'interaction à l'API de recommandation", exception);
        }
    }
}
