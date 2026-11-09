package com.gamesUP.gamesUP.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gamesUP.gamesUP.model.PurchaseLine;

public interface PurchaseLineRepository extends JpaRepository<PurchaseLine, Integer> {
}
