package com.gamesUP.gamesUP.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.repository.PurchaseRepository;

@ExtendWith(MockitoExtension.class)
class PurchaseCrudServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private com.gamesUP.gamesUP.repository.PurchaseLineRepository purchaseLineRepository;

    @Mock
    private RecommandationClient recommandationClient;

    @InjectMocks
    private PurchaseService purchaseService;

    @Test
    void enregistreUneCommande() {
        Purchase purchase = new Purchase();
        when(purchaseRepository.save(purchase)).thenReturn(purchase);

        assertSame(purchase, purchaseService.enregistrer(purchase));
        verify(purchaseRepository).save(purchase);
    }

    @Test
    void supprimeUneCommande() {
        Purchase purchase = new Purchase();
        when(purchaseRepository.findById(1L)).thenReturn(java.util.Optional.of(purchase));

        purchaseService.supprimer(1L);

        verify(purchaseRepository).deleteById(1L);
    }
}
