package com.gamesUP.gamesUP.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.service.PurchaseService;

@RestController
@RequestMapping("/commandes")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping
    public List<Purchase> getAll() {
        return purchaseService.getAll();
    }

    @GetMapping("/{id}")
    public Purchase findById(@PathVariable Long id) {
        return purchaseService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Purchase> enregistrer(@RequestBody Purchase purchase) {
        return ResponseEntity.status(201).body(purchaseService.enregistrer(purchase));
    }

    @PutMapping("/{id}")
    public Purchase modifier(@PathVariable Long id, @RequestBody Purchase purchase) {
        return purchaseService.modifier(id, purchase);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        purchaseService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

}
