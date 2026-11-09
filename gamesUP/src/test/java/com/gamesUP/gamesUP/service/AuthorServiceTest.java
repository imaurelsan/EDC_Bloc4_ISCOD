package com.gamesUP.gamesUP.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.repository.AuthorRepository;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void getAllRenvoieLesAuteurs() {
        List<Author> auteurs = List.of(new Author());
        when(authorRepository.findAll()).thenReturn(auteurs);

        assertSame(auteurs, authorService.getAll());
    }

    @Test
    void findByIdRenvoieLAuteur() {
        Author author = new Author();
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        assertSame(author, authorService.findById(1L));
    }

    @Test
    void findByIdLeveUneExceptionSiAbsent() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(JeuNonTrouveException.class, () -> authorService.findById(1L));
    }

    @Test
    void enregistrerSauvegardeLauteur() {
        Author author = new Author();
        when(authorRepository.save(author)).thenReturn(author);

        assertSame(author, authorService.enregistrer(author));
        verify(authorRepository).save(author);
    }

    @Test
    void modifierSauvegardeAvecLeBonId() {
        Author author = new Author();
        when(authorRepository.findById(1L)).thenReturn(Optional.of(new Author()));
        when(authorRepository.save(author)).thenReturn(author);

        authorService.modifier(1L, author);

        assertEquals(1L, author.id);
        verify(authorRepository).save(author);
    }

    @Test
    void supprimerSupprimeLauteur() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(new Author()));

        authorService.supprimer(1L);

        verify(authorRepository).deleteById(1L);
    }
}
