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

import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.PublisherRepository;

@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherService publisherService;

    @Test
    void getAllRenvoieLesEditeurs() {
        List<Publisher> editeurs = List.of(new Publisher());
        when(publisherRepository.findAll()).thenReturn(editeurs);

        assertSame(editeurs, publisherService.getAll());
    }

    @Test
    void findByIdRenvoieLEditeur() {
        Publisher publisher = new Publisher();
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));

        assertSame(publisher, publisherService.findById(1L));
    }

    @Test
    void findByIdLeveUneExceptionSiAbsent() {
        when(publisherRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(JeuNonTrouveException.class, () -> publisherService.findById(1L));
    }

    @Test
    void enregistrerSauvegardeLEditeur() {
        Publisher publisher = new Publisher();
        when(publisherRepository.save(publisher)).thenReturn(publisher);

        assertSame(publisher, publisherService.enregistrer(publisher));
        verify(publisherRepository).save(publisher);
    }

    @Test
    void modifierSauvegardeAvecLeBonId() {
        Publisher publisher = new Publisher();
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(new Publisher()));
        when(publisherRepository.save(publisher)).thenReturn(publisher);

        publisherService.modifier(1L, publisher);

        assertEquals(1L, publisher.getId());
        verify(publisherRepository).save(publisher);
    }

    @Test
    void supprimerSupprimeLEditeur() {
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(new Publisher()));

        publisherService.supprimer(1L);

        verify(publisherRepository).deleteById(1L);
    }
}
