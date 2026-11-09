package com.gamesUP.gamesUP.service;

public class JeuNonTrouveException extends RuntimeException {

    public JeuNonTrouveException(int id) {
        super("Jeu introuvable avec l'id : " + id);
    }
}
