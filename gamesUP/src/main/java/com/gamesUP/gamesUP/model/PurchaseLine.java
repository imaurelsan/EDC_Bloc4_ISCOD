package com.gamesUP.gamesUP.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PurchaseLine {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
    private int utilisateurId;
    private int jeuId;
    private double prix;

    public PurchaseLine() {
    }

    public PurchaseLine(int utilisateurId, int jeuId, double prix) {
        this.utilisateurId = utilisateurId;
        this.jeuId = jeuId;
        this.prix = prix;
    }

    public int getUtilisateurId() {
        return utilisateurId;
    }

    public int getJeuId() {
        return jeuId;
    }

    public double getPrix() {
        return prix;
    }
}
