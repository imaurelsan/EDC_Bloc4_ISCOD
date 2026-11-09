package com.gamesUP.gamesUP.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gamesUP.gamesUP.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
