package com.example.application.views.profile;

import com.example.application.data.Movie;
import com.example.application.data.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.services.MovieService;
import com.example.application.services.UserService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

@PageTitle("Profiili | Leffakerho")
@Route(value = "profile", layout = MainLayout.class)
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
public class ProfileView extends FormLayout {

    private final AuthenticatedUser authenticatedUser;
    private final UserService userService;
    private final MovieService movieService;

    private TextField nameField;
    private TextField emailField;

    private Grid<Movie> userMoviesGrid;

    public ProfileView(AuthenticatedUser authenticatedUser, UserService userService, MovieService movieService) {
        this.authenticatedUser = authenticatedUser;
        this.userService = userService;
        this.movieService = movieService;

        // Haetaan nykyinen käyttäjä
        User currentUser = authenticatedUser.get().orElse(null);
        if (currentUser != null) {
            // Käyttäjän tiedot esitäytettynä
            nameField = new TextField("Nimi", currentUser.getName());
            emailField = new TextField("Sähköposti", currentUser.getEmail());

            // Tallenna-painike
            Button saveButton = new Button("Tallenna", e -> {
                saveUserProfile();
            });

            // Elokuvat Grid
            userMoviesGrid = new Grid<>(Movie.class);
            userMoviesGrid.setColumns("title", "director", "releaseYear", "genre", "description");
            userMoviesGrid.addComponentColumn(movie -> {
                Button editButton = new Button("Muokkaa", e -> openEditMovieDialog(movie));
                Button deleteButton = new Button("Poista", e -> deleteMovie(movie));
                return new HorizontalLayout(editButton, deleteButton);
            });

            // Elokuvan lisäämisen painike
            Button addMovieButton = new Button("Lisää elokuva", e -> openAddMovieDialog());

            // Lisää kentät ja painikkeet lomakkeeseen
            add(nameField, emailField, saveButton, userMoviesGrid, addMovieButton);

            // Päivitä elokuvat Gridissä
            updateUserMovies(currentUser);
        } else {
            Notification.show("Et ole kirjautunut sisään.", 3000, Notification.Position.BOTTOM_START);
        }
    }

    private void saveUserProfile() {
        User currentUser = authenticatedUser.get().orElse(null);
        if (currentUser != null) {
            currentUser.setName(nameField.getValue());
            currentUser.setEmail(emailField.getValue());
            userService.save(currentUser);
            Notification.show("Profiilisi on päivitetty!", 3000, Notification.Position.BOTTOM_START);
        } else {
            Notification.show("Jotain meni pieleen. Yritä uudelleen.", 3000, Notification.Position.BOTTOM_START);
        }
    }

    private void openAddMovieDialog() {
        Dialog addMovieDialog = new Dialog();
        addMovieDialog.setWidth("400px");

        // Lomakekentät
        TextField titleField = new TextField("Elokuvan nimi");
        TextField directorField = new TextField("Ohjaaja");
        TextField genreField = new TextField("Genre");
        TextField yearField = new TextField("Julkaisuvuosi");
        TextArea descriptionField = new TextArea("Kuvaus");
        descriptionField.setHeight("150px");

        // Tallennuspainike
        Button saveButton = new Button("Tallenna", e -> {
            try {
                // Luodaan uusi elokuva
                Movie movie = new Movie();
                movie.setTitle(titleField.getValue());
                movie.setDirector(directorField.getValue());
                movie.setGenre(genreField.getValue());
                movie.setReleaseYear(Integer.parseInt(yearField.getValue()));
                movie.setDescription(descriptionField.getValue());

                // Tallennetaan elokuva
                Movie savedMovie = movieService.save(movie);

                // Lisätään elokuva käyttäjälle
                User currentUser = authenticatedUser.get().orElseThrow();
                userService.addMovieToUser(currentUser, savedMovie);

                // Päivitä näyttö
                updateUserMovies(currentUser);
                Notification.show("Elokuva lisätty onnistuneesti!");
                addMovieDialog.close();
            } catch (NumberFormatException ex) {
                Notification.show("Virheellinen vuosiluku");
            } catch (Exception ex) {
                Notification.show("Virhe: " + ex.getMessage());
            }
        });

        // Peruutuspainike
        Button cancelButton = new Button("Peruuta", e -> addMovieDialog.close());

        // Aseta painikkeet vierekkäin
        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        buttons.setSpacing(true);

        // Lisää komponentit dialogiin
        addMovieDialog.add(
                new VerticalLayout(
                        titleField,
                        directorField,
                        genreField,
                        yearField,
                        descriptionField,
                        buttons
                )
        );

        addMovieDialog.open();
    }

    private void updateUserMovies(User currentUser) {
        userMoviesGrid.setItems(currentUser.getOwnMovies());
    }

    private void openEditMovieDialog(Movie movie) {
        // Voit toteuttaa muokkauslogiikan tähän (esimerkiksi avata muokkausmodaali)
        Notification.show("Muokkauslogiikka menee tänne", 3000, Notification.Position.BOTTOM_START);
    }

    private void deleteMovie(Movie movie) {
        User currentUser = authenticatedUser.get().orElse(null);
        if (currentUser != null) {
            currentUser.getOwnMovies().remove(movie);
            userService.save(currentUser);
            movieService.delete(movie.getId());
            updateUserMovies(currentUser);
            Notification.show("Elokuva poistettu", 3000, Notification.Position.BOTTOM_START);
        }
    }
}