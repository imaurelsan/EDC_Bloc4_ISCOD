package com.gamesUP.gamesUP.dto;

import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Publisher;

public class GameDTO {

    private int id;
    private String nom;
    private double prix;
    private String description;
    private String auteur;
    private String genre;
    private Category category;
    private Publisher publisher;
    private int numEdition;

    public GameDTO() {
    }

    public GameDTO(int id, String nom, double prix, String description, String auteur,
            String genre, Category category, Publisher publisher, int numEdition) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.description = description;
        this.auteur = auteur;
        this.genre = genre;
        this.category = category;
        this.publisher = publisher;
        this.numEdition = numEdition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public int getNumEdition() {
        return numEdition;
    }

    public void setNumEdition(int numEdition) {
        this.numEdition = numEdition;
    }
}
