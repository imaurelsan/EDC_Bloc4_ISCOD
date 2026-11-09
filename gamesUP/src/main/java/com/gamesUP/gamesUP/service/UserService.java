package com.gamesUP.gamesUP.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User findById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
    }

    public User findByNom(String nom) {
        return userRepository.findByNom(nom)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
    }

    public User enregistrer(User user) {
        return userRepository.save(user);
    }

    public User modifier(int id, User user) {
        findById(id);
        user.setId(id);
        return userRepository.save(user);
    }

    public void supprimer(int id) {
        findById(id);
        userRepository.deleteById(id);
    }
}
