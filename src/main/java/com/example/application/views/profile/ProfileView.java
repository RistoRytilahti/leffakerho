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
public class ProfileView extends VerticalLayout {

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

        setSpacing(true);
        setPadding(true);
        setWidth("100%");

        // Haetaan nykyinen käyttäjä
        User currentUser = authenticatedUser.get().orElse(null);
        if (currentUser != null) {
            createProfileSection(currentUser);
            createMoviesSection(currentUser);
        } else {
            Notification.show("Et ole kirjautunut sisään.", 3000, Notification.Position.BOTTOM_START);
        }
    }

    private void createProfileSection(User currentUser) {
        FormLayout profileForm = new FormLayout();
        profileForm.setWidth("400px");

        nameField = new TextField("Nimi", currentUser.getName());
        emailField = new TextField("Sähköposti", currentUser.getEmail());

        Button saveButton = new Button("Tallenna", e -> saveUserProfile());
        saveButton.getStyle().set("margin-top", "10px");

        profileForm.add(nameField, emailField, saveButton);
        add(profileForm);
    }

    private void createMoviesSection(User currentUser) {
        VerticalLayout moviesSection = new VerticalLayout();
        moviesSection.setSpacing(true);
        moviesSection.setPadding(false);
        moviesSection.setWidth("100%");

        // Otsikko ja lisäysnappi
        HorizontalLayout header = new HorizontalLayout(new com.vaadin.flow.component.html.H2("Omat elokuvat"));
        header.setAlignItems(Alignment.CENTER);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setWidth("100%");

        Button addMovieButton = new Button("Lisää elokuva", e -> openAddMovieDialog());
        addMovieButton.getStyle().set("margin-left", "auto");
        header.add(addMovieButton);

        // Elokuvagrid
        userMoviesGrid = new Grid<>(Movie.class, false);
        userMoviesGrid.addColumn(Movie::getTitle).setHeader("Nimi").setAutoWidth(true);
        userMoviesGrid.addColumn(Movie::getDirector).setHeader("Ohjaaja").setAutoWidth(true);
        userMoviesGrid.addColumn(Movie::getReleaseYear).setHeader("Julkaisuvuosi").setAutoWidth(true);
        userMoviesGrid.addColumn(Movie::getGenre).setHeader("Genre").setAutoWidth(true);

        // Toiminnot sarake
        userMoviesGrid.addComponentColumn(movie -> {
            Button editButton = new Button("Muokkaa", e -> openEditMovieDialog(movie));
            Button deleteButton = new Button("Poista", e -> deleteMovie(movie));
            deleteButton.getStyle().set("color", "var(--lumo-error-color)");
            return new HorizontalLayout(editButton, deleteButton);
        }).setHeader("Toiminnot").setAutoWidth(true);

        userMoviesGrid.setWidth("100%");
        userMoviesGrid.getStyle().set("margin-top", "10px");

        // Päivitä grid käyttäjän elokuvilla
        updateUserMovies(currentUser);

        moviesSection.add(header, userMoviesGrid);
        add(moviesSection);
    }

    private void saveUserProfile() {
        User currentUser = authenticatedUser.get().orElse(null);
        if (currentUser != null) {
            currentUser.setName(nameField.getValue());
            currentUser.setEmail(emailField.getValue());
            try {
                userService.save(currentUser);
                Notification.show("Profiilisi on päivitetty!", 3000, Notification.Position.BOTTOM_START);
            } catch (Exception e) {
                Notification.show("Virhe: " + e.getMessage(), 3000, Notification.Position.BOTTOM_START);
            }
        } else {
            Notification.show("Jotain meni pieleen. Yritä uudelleen.", 3000, Notification.Position.BOTTOM_START);
        }
    }

    private void openAddMovieDialog() {
        Dialog addMovieDialog = new Dialog();
        addMovieDialog.setWidth("400px");

        // Lomakekentät
        TextField titleField = new TextField("Elokuvan nimi");
        titleField.setWidthFull();

        TextField directorField = new TextField("Ohjaaja");
        directorField.setWidthFull();

        TextField genreField = new TextField("Genre");
        genreField.setWidthFull();

        TextField yearField = new TextField("Julkaisuvuosi");
        yearField.setWidthFull();

        TextArea descriptionField = new TextArea("Kuvaus");
        descriptionField.setWidthFull();
        descriptionField.setHeight("150px");

        // Tallennuspainike
        Button saveButton = new Button("Tallenna", e -> {
            try {
                Movie movie = new Movie();
                movie.setTitle(titleField.getValue());
                movie.setDirector(directorField.getValue());
                movie.setGenre(genreField.getValue());
                movie.setReleaseYear(Integer.parseInt(yearField.getValue()));
                movie.setDescription(descriptionField.getValue());

                Movie savedMovie = movieService.save(movie);
                User currentUser = authenticatedUser.get().orElseThrow();
                currentUser.getOwnMovies().add(savedMovie);
                userService.save(currentUser);

                updateUserMovies(currentUser);
                Notification.show("Elokuva lisätty onnistuneesti!");
                addMovieDialog.close();
            } catch (NumberFormatException ex) {
                Notification.show("Virheellinen vuosiluku", 3000, Notification.Position.BOTTOM_START);
            } catch (Exception ex) {
                Notification.show("Virhe: " + ex.getMessage(), 3000, Notification.Position.BOTTOM_START);
            }
        });

        // Peruutuspainike
        Button cancelButton = new Button("Peruuta", e -> addMovieDialog.close());
        cancelButton.getStyle().set("margin-left", "auto");

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        buttons.setWidthFull();

        VerticalLayout dialogLayout = new VerticalLayout(
                titleField, directorField, genreField, yearField, descriptionField, buttons
        );
        dialogLayout.setSpacing(true);
        dialogLayout.setPadding(false);

        addMovieDialog.add(dialogLayout);
        addMovieDialog.open();
    }

    private void updateUserMovies(User currentUser) {
        userMoviesGrid.setItems(currentUser.getOwnMovies());
    }

    private void openEditMovieDialog(Movie movie) {
        Dialog editDialog = new Dialog();
        editDialog.setWidth("400px");

        TextField titleField = new TextField("Nimi", movie.getTitle(), "");
        TextField directorField = new TextField("Ohjaaja", movie.getDirector(), "");
        TextField yearField = new TextField("Julkaisuvuosi", String.valueOf(movie.getReleaseYear()), "");
        TextField genreField = new TextField("Genre", movie.getGenre(), "");
        TextArea descriptionField = new TextArea("Kuvaus", movie.getDescription(), "");
        descriptionField.setHeight("150px");

        Button saveButton = new Button("Tallenna", e -> {
            try {
                movie.setTitle(titleField.getValue());
                movie.setDirector(directorField.getValue());
                movie.setReleaseYear(Integer.parseInt(yearField.getValue()));
                movie.setGenre(genreField.getValue());
                movie.setDescription(descriptionField.getValue());

                movieService.save(movie);
                updateUserMovies(authenticatedUser.get().orElseThrow());
                Notification.show("Elokuva päivitetty!");
                editDialog.close();
            } catch (Exception ex) {
                Notification.show("Virhe: " + ex.getMessage());
            }
        });

        Button cancelButton = new Button("Peruuta", e -> editDialog.close());

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        buttons.setWidthFull();
        cancelButton.getStyle().set("margin-left", "auto");

        editDialog.add(new VerticalLayout(
                titleField, directorField, yearField, genreField, descriptionField, buttons
        ));
        editDialog.open();
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