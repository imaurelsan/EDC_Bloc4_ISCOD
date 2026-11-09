package com.gamesUP.gamesUP.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gamesUP.gamesUP.dto.PublisherDTO;
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.PublisherRepository;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<Publisher> getAll() {
        return publisherRepository.findAll();
    }

    public Publisher findById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new JeuNonTrouveException(id.intValue()));
    }

    public Publisher enregistrer(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    public Publisher modifier(Long id, Publisher publisher) {
        findById(id);
        publisher.setId(id);
        return publisherRepository.save(publisher);
    }

    public void supprimer(Long id) {
        findById(id);
        publisherRepository.deleteById(id);
    }

    public PublisherDTO versDTO(Publisher publisher) {
        return new PublisherDTO(publisher.getId(), publisher.getName());
    }

    public Publisher versEntite(PublisherDTO publisherDTO) {
        Publisher publisher = new Publisher();
        publisher.setId(publisherDTO.getId());
        publisher.setName(publisherDTO.getName());
        return publisher;
    }
}
