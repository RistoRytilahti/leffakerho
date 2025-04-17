package com.example.application.services;

import com.example.application.data.Movie;
import com.example.application.data.User;
import com.example.application.data.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    // Palauttaa käyttäjän ID:n perusteella
    public Optional<User> get(Long id) {
        return repository.findById(id);
    }

    // Tallentaa käyttäjän tietokantaan (voi luoda tai päivittää)
    public User save(User entity) {
        if (entity.getEmail() == null || entity.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Sähköpostiosoite ei voi olla tyhjä");
        }

        // Check if email exists only for new users or when email changes
        Optional<User> existingUser = entity.getId() != null ? repository.findById(entity.getId()) : Optional.empty();

        if (existingUser.isPresent()) {
            // Only check email if it's being changed
            if (!entity.getEmail().equals(existingUser.get().getEmail()) &&
                    repository.existsByEmail(entity.getEmail())) {
                throw new IllegalArgumentException("Sähköposti on jo käytössä");
            }
        } else if (repository.existsByEmail(entity.getEmail())) {
            throw new IllegalArgumentException("Sähköposti on jo käytössä");
        }

        return repository.save(entity);
    }

    // Lisää tämä uusi metodi vain elokuvien lisäämiseen
    public User addMovieToUser(User user, Movie movie) {
        user.getOwnMovies().add(movie);
        return repository.save(user); // Tämä kutsuu suoraan repositorya välttääkseen turhat tarkistukset
    }

    // Poistaa käyttäjän ID:n perusteella
    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Käyttäjää ei löydy ID:llä: " + id);
        }
    }

    // Palauttaa kaikki käyttäjät sivutettuna
    public Page<User> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // Palauttaa käyttäjät suodatettuna
    public Page<User> list(Pageable pageable, Specification<User> filter) {
        return repository.findAll(filter, pageable);
    }

    // Palauttaa käyttäjien kokonaismäärän
    public int count() {
        return (int) repository.count();
    }

    // Hakee käyttäjän sähköpostiosoitteen perusteella
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    // Hakee käyttäjän nimen perusteella
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }
}
