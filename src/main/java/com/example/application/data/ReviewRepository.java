package com.example.application.data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {

    List<Review> findByMovie(Movie movie);

    Optional<Review> findByMovieAndReviewer(Movie movie, User reviewer);

    List<Review> findByReviewer(User reviewer);
}
