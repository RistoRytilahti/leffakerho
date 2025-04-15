package com.example.application.services;

import com.example.application.data.Movie;
import com.example.application.data.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

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
        return movieRepository.findByAverageRatingGreaterThanEqual(minRating); // Käytetään keskiarvokyselyä
    }

    // Elokuvan tallentaminen
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    // Elokuvan poistaminen
    public void delete(Long movieId) {
        movieRepository.deleteById(movieId);
    }
}
