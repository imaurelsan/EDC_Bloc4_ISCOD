package com.gamesUP.gamesUP.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gamesUP.gamesUP.dto.AuthorDTO;
import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.repository.AuthorRepository;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    public Author findById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new JeuNonTrouveException(id.intValue()));
    }

    public Author enregistrer(Author author) {
        return authorRepository.save(author);
    }

    public Author modifier(Long id, Author author) {
        findById(id);
        author.id = id;
        return authorRepository.save(author);
    }

    public void supprimer(Long id) {
        findById(id);
        authorRepository.deleteById(id);
    }

    public AuthorDTO versDTO(Author author) {
        return new AuthorDTO(author.id, author.name);
    }

    public Author versEntite(AuthorDTO authorDTO) {
        Author author = new Author();
        author.id = authorDTO.getId();
        author.name = authorDTO.getName();
        return author;
    }
}
