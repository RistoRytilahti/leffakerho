package com.example.application.services;

import com.example.application.data.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final WatchListEntryRepository watchListEntryRepository;
    private final ReviewRepository reviewRepository;

    public UserService(UserRepository repository, WatchListEntryRepository watchListEntryRepository, ReviewRepository reviewRepository) {
        this.repository = repository;
        this.watchListEntryRepository = watchListEntryRepository;
        this.reviewRepository = reviewRepository;
    }

    public Optional<User> get(Long id) {
        return repository.findById(id);
    }

    public User save(User entity) {
        if (entity.getEmail() == null || entity.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Sähköpostiosoite ei voi olla tyhjä");
        }

        Optional<User> existingUser = entity.getId() != null ? repository.findById(entity.getId()) : Optional.empty();

        if (existingUser.isPresent()) {
            if (!entity.getEmail().equals(existingUser.get().getEmail()) &&
                    repository.existsByEmail(entity.getEmail())) {
                throw new IllegalArgumentException("Sähköposti on jo käytössä");
            }
        } else if (repository.existsByEmail(entity.getEmail())) {
            throw new IllegalArgumentException("Sähköposti on jo käytössä");
        }

        return repository.save(entity);
    }

    public User addMovieToUser(User user, Movie movie) {
        user.getOwnMovies().add(movie);
        return repository.save(user);
    }

    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Käyttäjää ei löydy ID:llä: " + id);
        }
    }

    @Transactional
    public void deleteCompletely(Long userId) {
        repository.findById(userId).ifPresent(user -> {
            // Poista watchlist-viittaukset
            watchListEntryRepository.deleteByUser(user);

            // Poista käyttäjän arvostelut
            List<Review> reviews = reviewRepository.findByReviewer(user);
            for (Review review : reviews) {
                review.setReviewer(null);  // Poista käyttäjäviittaus arvostelusta
                reviewRepository.save(review);  // Tallennetaan arvostelu ilman käyttäjäviittausta
            }

            // Tyhjennä mahdolliset omat elokuvat (jos ei haluta poistaa elokuvia tietokannasta)
            user.getOwnMovies().clear();
            repository.save(user);

            // Poista itse käyttäjä
            repository.delete(user);
        });
    }


    public Page<User> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<User> list(Pageable pageable, Specification<User> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }
}
