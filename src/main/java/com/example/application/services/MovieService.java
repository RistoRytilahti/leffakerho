package com.example.application.services;

import com.example.application.data.Movie;
import com.example.application.data.MovieRepository;
import com.example.application.data.User;
import com.example.application.data.UserRepository;
import com.example.application.data.WatchListEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private WatchListEntryRepository watchListEntryRepository;

    @Autowired
    private UserRepository userRepository;

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    public List<Movie> findMovies(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return movieRepository.findAll();
        } else {
            return movieRepository.findByTitleContainingIgnoreCase(filterText);
        }
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public List<Movie> findMoviesByMinRating(double minRating) {
        return movieRepository.findByAverageRatingGreaterThanEqual(minRating);
    }

    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    // 🧹 Poistaa kaikki viittaukset ja itse elokuvan
    public void deleteCompletely(Long movieId) {
        movieRepository.findById(movieId).ifPresent(movie -> {
            // 1. Poista watchlist-viittaukset
            watchListEntryRepository.deleteByMovie(movie);

            // 2. Poista omistussuhteet käyttäjiltä
            List<User> users = userRepository.findAll();
            for (User user : users) {
                if (user.getOwnMovies().remove(movie)) {
                    userRepository.save(user);
                }
            }

            // 3. Poista elokuva
            movieRepository.delete(movie);
        });
    }
}
