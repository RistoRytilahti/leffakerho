package com.example.application.views.movies;

import com.example.application.data.Movie;
import com.example.application.data.Review;
import com.example.application.services.MovieService;
import com.example.application.services.ReviewService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@PageTitle("Elokuvan tiedot | Leffakerho")
@Route(value = "movie/:movieId", layout = MainLayout.class)
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
public class MovieDetailView extends VerticalLayout implements BeforeEnterObserver {

    private final MovieService movieService;
    private final ReviewService reviewService;

    public MovieDetailView(MovieService movieService, ReviewService reviewService) {
        this.movieService = movieService;
        this.reviewService = reviewService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        removeAll(); // Tyhjennetään vanhat komponentit

        String movieIdParam = event.getRouteParameters().get("movieId").orElse(null);

        if (movieIdParam != null) {
            try {
                Long movieId = Long.valueOf(movieIdParam);
                movieService.findById(movieId).ifPresentOrElse(movie -> {

                    // Vasen sarake: elokuvan tiedot
                    VerticalLayout movieInfoLayout = new VerticalLayout();
                    movieInfoLayout.add(
                            new H1(movie.getTitle()),
                            new Paragraph("Ohjaaja: " + movie.getDirector()),
                            new Paragraph("Julkaisuvuosi: " + movie.getReleaseYear()),
                            new Paragraph("Genre: " + movie.getGenre()),
                            new Paragraph("Kuvaus: " + movie.getDescription())
                    );

                    // Oikea sarake: arvostelut
                    VerticalLayout reviewLayout = new VerticalLayout();
                    List<Review> reviews = reviewService.findByMovie(movie);


                    if (!reviews.isEmpty()) {
                        double averageRating = reviews.stream()
                                .mapToInt(Review::getRating)
                                .average()
                                .orElse(0.0);

                        reviewLayout.add(new H1("Arvostelut (" + String.format("%.1f", averageRating) + "/5)"));

                        for (Review review : reviews) {
                            String reviewerName = (review.getReviewer() != null) ? review.getReviewer().getName() : "Tuntematon";
                            reviewLayout.add(
                                    new Paragraph("⭑ " + review.getRating() + "/5 — " + reviewerName),
                                    new Paragraph(review.getComment())
                            );
                        }
                    } else {
                        reviewLayout.add(new H1("Arvostelut"), new Paragraph("Ei arvosteluita vielä."));
                    }

                    // Molemmat sarakkeet vierekkäin
                    HorizontalLayout contentLayout = new HorizontalLayout(movieInfoLayout, reviewLayout);
                    contentLayout.setWidthFull();
                    movieInfoLayout.setWidth("50%");
                    reviewLayout.setWidth("50%");

                    add(contentLayout);

                }, () -> add(new Paragraph("Elokuvaa ei löytynyt.")));
            } catch (NumberFormatException e) {
                add(new Paragraph("Virheellinen elokuvan ID."));
            }
        } else {
            add(new Paragraph("Elokuvan ID puuttuu."));
        }

        // Takaisin-nappi
        Button backButton = new Button("Takaisin", e -> getUI().ifPresent(ui -> ui.navigate(MoviesView.class)));
        add(backButton);
    }
}
