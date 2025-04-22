package com.example.application.views.movies;

import com.example.application.data.Movie;
import com.example.application.services.MovieService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Elokuvat | Leffakerho")
@Route(value = "movies", layout = MainLayout.class)
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
public class MoviesView extends VerticalLayout {

    private final MovieService movieService;
    private final Grid<Movie> grid = new Grid<>(Movie.class);
    private final TextField titleFilter = new TextField("Nimi");
    private final TextField directorFilter = new TextField("Ohjaaja");
    private final TextField genreFilter = new TextField("Genre");
    private final IntegerField yearFilter = new IntegerField("Vuosi");

    public MoviesView(MovieService movieService) {
        this.movieService = movieService;

        setSizeFull();
        setSpacing(true);
        setPadding(true);

        H1 header = new H1("Kaikki elokuvat:");
        add(header);

        titleFilter.setPlaceholder("Hae nimen mukaan");
        directorFilter.setPlaceholder("Hae ohjaajan mukaan");
        genreFilter.setPlaceholder("Hae genren mukaan");
        yearFilter.setPlaceholder("Hae julkaisuvuoden mukaan");

        titleFilter.addValueChangeListener(e -> updateList());
        directorFilter.addValueChangeListener(e -> updateList());
        genreFilter.addValueChangeListener(e -> updateList());
        yearFilter.addValueChangeListener(e -> updateList());

        grid.setColumns("title", "director", "releaseYear", "genre");
        grid.getColumnByKey("title").setHeader("Nimi");
        grid.getColumnByKey("director").setHeader("Ohjaaja");
        grid.getColumnByKey("releaseYear").setHeader("Julkaisuvuosi");
        grid.getColumnByKey("genre").setHeader("Genre");

        // Poistonappula vain Adminille
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            grid.addComponentColumn(movie -> {
                Button deleteButton = new Button("Poista", e -> {
                    // Varmistetaan poisto ennen
                    ConfirmDialog confirmDialog = new ConfirmDialog("Varmista poisto",
                            "Oletko varma, että haluat poistaa elokuvan ja kaikki siihen liittyvät tiedot?", "Poista", event -> {
                        movieService.deleteCompletely(movie.getId());
                        Notification.show("Elokuva poistettu.");
                        updateList();
                    }, "Peruuta", event -> {
                    });
                    confirmDialog.open();
                });
                return deleteButton;
            }).setHeader("Toiminnot");
        }

        grid.asSingleSelect().addValueChangeListener(e -> {
            Movie selectedMovie = e.getValue();
            if (selectedMovie != null) {
                getUI().ifPresent(ui -> ui.navigate("movie/" + selectedMovie.getId()));
            }
        });

        HorizontalLayout filters = new HorizontalLayout(titleFilter, directorFilter, genreFilter, yearFilter);
        add(filters, grid);
        updateList();
    }

    private void updateList() {
        List<Movie> movies = movieService.findAll(); // Hae kaikki elokuvat

        // Suodatetaan elokuvat muiden hakuehtojen mukaan (nimi, ohjaaja, genre, vuosi)
        movies = movies.stream()
                .filter(movie -> titleFilter.isEmpty() || movie.getTitle().toLowerCase().contains(titleFilter.getValue().toLowerCase()))
                .filter(movie -> directorFilter.isEmpty() || movie.getDirector().toLowerCase().contains(directorFilter.getValue().toLowerCase()))
                .filter(movie -> genreFilter.isEmpty() || movie.getGenre().toLowerCase().contains(genreFilter.getValue().toLowerCase()))
                .filter(movie -> yearFilter.isEmpty() || movie.getReleaseYear() == yearFilter.getValue())
                .collect(Collectors.toList());

        grid.setItems(movies);
    }
}
