package com.gamesUP.gamesUP.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gamesUP.gamesUP.model.PurchaseLine;
import com.gamesUP.gamesUP.repository.PurchaseLineRepository;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {

    @Mock
    private PurchaseLineRepository purchaseLineRepository;

    @Mock
    private RecommandationClient recommandationClient;

    @InjectMocks
    private PurchaseService purchaseService;

    private PurchaseLine achatEnregistre;

    @BeforeEach
    void preparerAchat() {
        achatEnregistre = new PurchaseLine(1, 2, 39.99);
        when(purchaseLineRepository.save(any(PurchaseLine.class))).thenReturn(achatEnregistre);
    }

    @Test
    void enregistreAchatEtEnvoieInteraction() {
        PurchaseLine resultat = purchaseService.enregistrerAchat(1, 2, 39.99);

        assertSame(achatEnregistre, resultat);
        verify(purchaseLineRepository).save(any(PurchaseLine.class));
        verify(recommandationClient).envoyerInteraction(1, 2);
    }

    @Test
    void gardeAchatSiEnvoiEchoue() {
        doThrow(new RuntimeException("API indisponible"))
                .when(recommandationClient).envoyerInteraction(1, 2);

        PurchaseLine resultat = purchaseService.enregistrerAchat(1, 2, 39.99);

        assertSame(achatEnregistre, resultat);
        verify(purchaseLineRepository).save(any(PurchaseLine.class));
        verify(recommandationClient).envoyerInteraction(1, 2);
    }
}
