package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchListEntryRepository extends JpaRepository<WatchListEntry, Long> {
    List<WatchListEntry> findByUser(User user);
    List<WatchListEntry> findByMovie(Movie movie);
    boolean existsByUserAndMovie(User user, Movie movie);
}
