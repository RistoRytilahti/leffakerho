package com.example.application.views.reviews;

import com.example.application.data.Movie;
import com.example.application.data.Review;
import com.example.application.services.MovieService;
import com.example.application.services.ReviewService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Arvostelut")
@Route(value = "reviews", layout = MainLayout.class)
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
public class ReviewsView extends VerticalLayout {

    private final MovieService movieService;
    private final ReviewService reviewService;
    private final Grid<Review> grid = new Grid<>(Review.class);

    public ReviewsView(MovieService movieService, ReviewService reviewService) {
        this.movieService = movieService;
        this.reviewService = reviewService;

        setSizeFull();

        grid.removeAllColumns(); // Poistetaan oletussarakkeet, jotta voidaan lisätä räätälöidyt

        grid.addColumn(review -> review.getMovie().getTitle()).setHeader("Elokuva");
        grid.addColumn(Review::getRating).setHeader("Arvosana");
        grid.addColumn(Review::getComment).setHeader("Kommentti");

        grid.setItems(reviewService.findAllReviews());

        Button addReviewButton = new Button("Lisää arvostelu", e -> {
            // Toiminnallisuus arvostelun lisäämiseen
        });

        add(addReviewButton, grid);
    }

}
