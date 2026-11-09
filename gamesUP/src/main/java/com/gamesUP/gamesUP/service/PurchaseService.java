package com.gamesUP.gamesUP.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.PurchaseLine;
import com.gamesUP.gamesUP.repository.PurchaseLineRepository;
import com.gamesUP.gamesUP.repository.PurchaseRepository;

@Service
public class PurchaseService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseService.class);

    private final PurchaseLineRepository purchaseLineRepository;
    private final PurchaseRepository purchaseRepository;
    private final RecommandationClient recommandationClient;

    public PurchaseService(PurchaseLineRepository purchaseLineRepository,
            PurchaseRepository purchaseRepository, RecommandationClient recommandationClient) {
        this.purchaseLineRepository = purchaseLineRepository;
        this.purchaseRepository = purchaseRepository;
        this.recommandationClient = recommandationClient;
    }

    public List<Purchase> getAll() {
        return purchaseRepository.findAll();
    }

    public Purchase findById(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Commande introuvable"));
    }

    public Purchase enregistrer(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public Purchase modifier(Long id, Purchase purchase) {
        findById(id);
        purchase.setId(id);
        return purchaseRepository.save(purchase);
    }

    public void supprimer(Long id) {
        findById(id);
        purchaseRepository.deleteById(id);
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
