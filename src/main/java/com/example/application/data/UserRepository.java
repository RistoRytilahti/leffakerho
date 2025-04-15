package com.example.application.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    // Hakee käyttäjän sähköpostin perusteella
    Optional<User> findByEmail(String email);

    // Tarkistaa, onko käyttäjä jo olemassa annetulla sähköpostilla
    boolean existsByEmail(String email);

    // Voit myös lisätä muita hakuja, kuten:
    Optional<User> findByUsername(String username);

}
