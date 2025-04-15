package com.example.application.services;

import com.example.application.data.Movie;
import com.example.application.data.User;
import com.example.application.data.WatchListEntry;
import com.example.application.data.WatchListEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WatchListEntryService {

    private final WatchListEntryRepository watchListEntryRepository;

    public WatchListEntryService(WatchListEntryRepository watchListEntryRepository) {
        this.watchListEntryRepository = watchListEntryRepository;
    }

    public List<WatchListEntry> findAll() {
        return watchListEntryRepository.findAll();
    }

    public List<WatchListEntry> findByUser(User user) {
        return watchListEntryRepository.findByUser(user);
    }

    public List<WatchListEntry> findByMovie(Movie movie) {
        return watchListEntryRepository.findByMovie(movie);
    }

    public boolean existsByUserAndMovie(User user, Movie movie) {
        return watchListEntryRepository.existsByUserAndMovie(user, movie);
    }

    public Optional<WatchListEntry> findById(Long id) {
        return watchListEntryRepository.findById(id);
    }

    public WatchListEntry save(WatchListEntry entry) {
        return watchListEntryRepository.save(entry);
    }

    public void delete(WatchListEntry entry) {
        watchListEntryRepository.delete(entry);
    }
}
