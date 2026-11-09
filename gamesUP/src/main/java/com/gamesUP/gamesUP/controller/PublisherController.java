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
import org.springframework.security.access.prepost.PreAuthorize;

import com.gamesUP.gamesUP.dto.PublisherDTO;
import com.gamesUP.gamesUP.service.PublisherService;

@RestController
@RequestMapping("/editeurs")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public List<PublisherDTO> getAll() {
        return publisherService.getAll().stream().map(publisherService::versDTO).toList();
    }

    @GetMapping("/{id}")
    public PublisherDTO findById(@PathVariable Long id) {
        return publisherService.versDTO(publisherService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublisherDTO> enregistrer(@RequestBody PublisherDTO publisherDTO) {
        return ResponseEntity.status(201)
                .body(publisherService.versDTO(publisherService.enregistrer(publisherService.versEntite(publisherDTO))));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PublisherDTO modifier(@PathVariable Long id, @RequestBody PublisherDTO publisherDTO) {
        return publisherService.versDTO(publisherService.modifier(id, publisherService.versEntite(publisherDTO)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        publisherService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
