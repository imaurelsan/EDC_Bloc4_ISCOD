package com.gamesUP.gamesUP.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gamesUP.gamesUP.service.JeuNonTrouveException;

@RestControllerAdvice
public class GestionnaireErreurs {

    @ExceptionHandler(JeuNonTrouveException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String gererJeuNonTrouve(JeuNonTrouveException exception) {
        return exception.getMessage();
    }
}
