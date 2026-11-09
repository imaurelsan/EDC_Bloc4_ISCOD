package com.gamesUP.gamesUP.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gamesUP.gamesUP.model.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
}
