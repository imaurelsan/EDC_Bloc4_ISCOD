package com.gamesUP.gamesUP.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gamesUP.gamesUP.model.PurchaseLine;
import com.gamesUP.gamesUP.repository.PurchaseLineRepository;

@Service
public class PurchaseService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseService.class);

    private final PurchaseLineRepository purchaseLineRepository;
    private final RecommandationClient recommandationClient;

    public PurchaseService(PurchaseLineRepository purchaseLineRepository,
            RecommandationClient recommandationClient) {
        this.purchaseLineRepository = purchaseLineRepository;
        this.recommandationClient = recommandationClient;
    }

    public PurchaseLine enregistrerAchat(int utilisateurId, int jeuId, double prix) {
        PurchaseLine achat = new PurchaseLine(utilisateurId, jeuId, prix);
        PurchaseLine achatEnregistre = purchaseLineRepository.save(achat);

        try {
            recommandationClient.envoyerInteraction(utilisateurId, jeuId);
        } catch (RuntimeException exception) {
            logger.error("Impossible d'envoyer l'achat à l'API de recommandation", exception);
        }

        return achatEnregistre;
    }
}
