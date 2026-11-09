package com.gamesUP.gamesUP.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gamesUP.gamesUP.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
