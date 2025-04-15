package com.example.application.views.movies;

import com.example.application.data.Movie;
import com.example.application.services.MovieService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Elokuvan tiedot | Leffakerho")
@Route(value = "movie/:movieId", layout = MainLayout.class)
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
public class MovieDetailView extends VerticalLayout implements BeforeEnterObserver {

    private final MovieService movieService;
    private Long movieId;

    public MovieDetailView(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Haetaan movieId URL:stä
        String movieIdParam = event.getRouteParameters().get("movieId").orElse(null);

        if (movieIdParam != null) {
            try {
                movieId = Long.valueOf(movieIdParam);
                Movie movie = movieService.findById(movieId).orElse(null);
                if (movie != null) {
                    add(
                            new H1(movie.getTitle()),
                            new Paragraph("Ohjaaja: " + movie.getDirector()),
                            new Paragraph("Julkaisuvuosi: " + movie.getReleaseYear()),
                            new Paragraph("Genre: " + movie.getGenre()),
                            new Paragraph("Kuvaus: " + movie.getDescription())
                    );
                } else {
                    add(new Paragraph("Elokuvaa ei löytynyt."));
                }
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
