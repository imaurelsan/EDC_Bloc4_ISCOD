package com.gamesUP.gamesUP.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void enregistreUnUtilisateur() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        assertSame(user, userService.enregistrer(user));
        verify(userRepository).save(user);
    }

    @Test
    void trouveUnUtilisateurParSonNom() {
        User user = new User();
        when(userRepository.findByNom("client")).thenReturn(Optional.of(user));

        assertSame(user, userService.findByNom("client"));
    }

    @Test
    void supprimeUnUtilisateur() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.supprimer(1);

        verify(userRepository).deleteById(1);
    }
}
