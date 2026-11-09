package com.gamesUP.gamesUP.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.service.UserService;

@RestController
@RequestMapping("/utilisateurs")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/me")
    public User getCurrentUser(Authentication authentication) {
        return userService.findByNom(authentication.getName());
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable int id) {
        return userService.findById(id);
    }

    @PostMapping
    public ResponseEntity<User> enregistrer(@RequestBody User user) {
        return ResponseEntity.status(201).body(userService.enregistrer(user));
    }

    @PutMapping("/{id}")
    public User modifier(@PathVariable int id, @RequestBody User user) {
        return userService.modifier(id, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable int id) {
        userService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
