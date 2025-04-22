package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Hae elokuvia, joiden nimi sisältää hakutekstin (ei ole kirjainkokosidonnainen)
    List<Movie> findByTitleContainingIgnoreCase(String title);

    // Hae elokuvia, joiden arvosana on suurempi tai yhtä suuri kuin minRating
    @Query("SELECT m FROM Movie m " +
            "JOIN Review r ON r.movie = m " +
            "GROUP BY m " +
            "HAVING AVG(r.rating) >= :rating")
    List<Movie> findByAverageRatingGreaterThanEqual(@Param("rating") double rating);

    @Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.reviews")
    List<Movie> findAllWithReviews();


}


