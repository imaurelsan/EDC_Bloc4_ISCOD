package com.gamesUP.gamesUP.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "utilisateur")
public class User {
   
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
    private String nom;

}
