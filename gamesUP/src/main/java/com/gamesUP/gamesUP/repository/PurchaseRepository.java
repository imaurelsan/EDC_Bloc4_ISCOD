package com.gamesUP.gamesUP.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gamesUP.gamesUP.model.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
