package com.example.application.views.watchlist;

import com.example.application.data.*;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Route(value = "watchlist", layout = MainLayout.class)
@PageTitle("Suosikit | Leffakerho")
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
public class WatchListView extends VerticalLayout {

    private final WatchListEntryRepository watchListEntryRepository;
    private final MovieRepository movieRepository;
    private final AuthenticatedUser authenticatedUser;

    private final ComboBox<Movie> movieSelect = new ComboBox<>("Valitse elokuva");
    private final Button addButton = new Button("Lisää suosikkeihin");
    private final Grid<WatchListEntry> watchListGrid = new Grid<>(WatchListEntry.class);

    @Autowired
    public WatchListView(WatchListEntryRepository watchListEntryRepository,
                         MovieRepository movieRepository,
                         AuthenticatedUser authenticatedUser) {
        this.watchListEntryRepository = watchListEntryRepository;
        this.movieRepository = movieRepository;
        this.authenticatedUser = authenticatedUser;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Oma katselulista"));

        configureForm();
        configureGrid();

        updateGrid();
    }

    private void configureForm() {
        movieSelect.setItems(movieRepository.findAll());
        movieSelect.setItemLabelGenerator(Movie::getTitle);

        addButton.addClickListener(e -> {
            Optional<User> maybeUser = authenticatedUser.get();
            if (maybeUser.isPresent() && movieSelect.getValue() != null) {
                User user = maybeUser.get();
                Movie movie = movieSelect.getValue();

                if (!watchListEntryRepository.existsByUserAndMovie(user, movie)) {
                    WatchListEntry entry = new WatchListEntry();
                    entry.setUser(user);
                    entry.setMovie(movie);
                    watchListEntryRepository.save(entry);
                    Notification.show("Elokuva lisätty suosikkeihin.");
                    updateGrid();
                    movieSelect.clear();
                } else {
                    Notification.show("Elokuva on jo suosikeissa.");
                }
            }
        });

        add(new HorizontalLayout(movieSelect, addButton));
    }

    private void configureGrid() {
        watchListGrid.removeAllColumns();
        watchListGrid.addColumn(entry -> entry.getMovie().getTitle()).setHeader("Elokuva");
        watchListGrid.addColumn(entry -> entry.getAddedAt().toLocalDate()).setHeader("Lisätty");

        // Katsottu / ei katsottu -status ja toggle-nappi
        watchListGrid.addColumn(entry -> entry.isWatched() ? "Kyllä" : "Ei")
                .setHeader("Katsottu");

        watchListGrid.addComponentColumn(entry -> {
            Button toggleWatched = new Button(entry.isWatched() ? "Merkitse katsomattomaksi" : "Merkitse katsotuksi");
            toggleWatched.addClickListener(e -> {
                entry.setWatched(!entry.isWatched());
                watchListEntryRepository.save(entry);
                updateGrid(); // Päivitetään näkymä
            });
            return toggleWatched;
        }).setHeader("Tila");

        watchListGrid.addComponentColumn(entry -> {
            Button deleteButton = new Button("Poista", e -> {
                watchListEntryRepository.delete(entry);
                Notification.show("Elokuva poistettu suosikeista.");
                updateGrid();
            });
            return deleteButton;
        }).setHeader("Toiminnot");

        watchListGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        watchListGrid.setSizeFull();
        add(watchListGrid);
    }

    private void updateGrid() {
        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            List<WatchListEntry> entries = watchListEntryRepository.findByUser(user);
            watchListGrid.setItems(entries);
        }
    }
}
