package com.gamesUP.gamesUP.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gamesUP.gamesUP.dto.AuthorDTO;
import com.gamesUP.gamesUP.service.AuthorService;

@RestController
@RequestMapping("/auteurs")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<AuthorDTO> getAll() {
        return authorService.getAll().stream().map(authorService::versDTO).toList();
    }

    @GetMapping("/{id}")
    public AuthorDTO findById(@PathVariable Long id) {
        return authorService.versDTO(authorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> enregistrer(@RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.status(201)
                .body(authorService.versDTO(authorService.enregistrer(authorService.versEntite(authorDTO))));
    }

    @PutMapping("/{id}")
    public AuthorDTO modifier(@PathVariable Long id, @RequestBody AuthorDTO authorDTO) {
        return authorService.versDTO(authorService.modifier(id, authorService.versEntite(authorDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        authorService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
