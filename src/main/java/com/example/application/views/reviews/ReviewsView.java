package com.example.application.views.reviews;

import com.example.application.data.*;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Route(value = "reviews", layout = MainLayout.class)
@PageTitle("Arvostelut | Leffakerho")
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
public class ReviewsView extends VerticalLayout {

    private final ReviewRepository reviewRepository;
    private final AuthenticatedUser authenticatedUser;

    private final Grid<Review> reviewGrid = new Grid<>(Review.class);
    private final ComboBox<Movie> movieFilter = new ComboBox<>("Suodata elokuvan mukaan");
    private final Button clearFilterButton = new Button("Tyhjennä suodatin");

    private final ComboBox<Movie> movieSelect = new ComboBox<>("Valitse elokuva");
    private final TextArea commentField = new TextArea("Kommentti");
    private final TextField ratingField = new TextField("Arvosana (1-5)");
    private final Button submitReviewButton = new Button("Lähetä arvostelu");
    private final MovieRepository movieRepository;


    private boolean isAdmin;

    @Autowired
    public ReviewsView(ReviewRepository reviewRepository, AuthenticatedUser authenticatedUser, MovieRepository movieRepository) {
        this.reviewRepository = reviewRepository;
        this.authenticatedUser = authenticatedUser;
        this.movieRepository = movieRepository;


        setSizeFull();
        setPadding(true);
        setSpacing(true);

        this.isAdmin = authenticatedUser.get()
                .map(user -> user.getRoles().stream()
                        .anyMatch(role -> role.name().equals("ADMIN")))
                .orElse(false);

        add(new H2("Arvostelut"));

        configureGrid();
        configureFilter();
        configureForm();

        updateGrid(null);
    }

    private void configureGrid() {
        reviewGrid.setColumns("movie.title", "reviewer.username", "rating", "comment");
        reviewGrid.getColumnByKey("movie.title").setHeader("Elokuva");
        reviewGrid.getColumnByKey("reviewer.username").setHeader("Käyttäjä");
        reviewGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        if (isAdmin) {
            reviewGrid.addComponentColumn(review -> {
                Button edit = new Button("Muokkaa", e -> openEditDialog(review));
                Button delete = new Button("Poista", e -> {
                    reviewRepository.delete(review);
                    updateGrid(movieFilter.getValue());
                    Notification.show("Arvostelu poistettu.");
                });
                return new HorizontalLayout(edit, delete);
            }).setHeader("Toiminnot");
        }

        reviewGrid.setSizeFull();
        add(reviewGrid);
    }

    private void configureFilter() {
        List<Movie> movies = reviewRepository.findAll().stream()
                .map(Review::getMovie)
                .distinct()
                .toList();

        movieFilter.setItems(movies);
        movieFilter.setItemLabelGenerator(Movie::getTitle);

        movieFilter.addValueChangeListener(e -> updateGrid(e.getValue()));
        clearFilterButton.addClickListener(e -> {
            movieFilter.clear();
            updateGrid(null);
        });

        add(new HorizontalLayout(movieFilter, clearFilterButton));
    }

    private void configureForm() {
        List<Movie> movies = movieRepository.findAll(); // Hae kaikki elokuvat
        movieSelect.setItems(movies);
        movieSelect.setItemLabelGenerator(Movie::getTitle);

        submitReviewButton.addClickListener(e -> addReview());

        add(new H2("Lisää arvostelu"));
        add(movieSelect, ratingField, commentField, submitReviewButton);
    }

    private void addReview() {
        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isEmpty()) {
            Notification.show("Kirjaudu sisään arvostellaksesi.");
            return;
        }

        User currentUser = maybeUser.get();
        Movie selectedMovie = movieSelect.getValue();
        String ratingText = ratingField.getValue();

        if (selectedMovie == null || ratingText == null || ratingText.isEmpty()) {
            Notification.show("Täytä kaikki kentät.");
            return;
        }

        int rating;
        try {
            rating = Integer.parseInt(ratingText);
        } catch (NumberFormatException e) {
            Notification.show("Arvosanan tulee olla numero 1–5.");
            return;
        }

        if (rating < 1 || rating > 5) {
            Notification.show("Arvosanan tulee olla välillä 1–5.");
            return;
        }

        boolean exists = reviewRepository.findByMovieAndReviewer(selectedMovie, currentUser).isPresent();
        if (exists) {
            Notification.show("Olet jo arvostellut tämän elokuvan.");
            return;
        }

        Review review = new Review();
        review.setMovie(selectedMovie);
        review.setReviewer(currentUser);
        review.setRating(rating);
        review.setComment(commentField.getValue());

        reviewRepository.save(review);
        Notification.show("Arvostelu tallennettu.");
        movieSelect.clear();
        ratingField.clear();
        commentField.clear();
        updateGrid(movieFilter.getValue());
    }

    private void openEditDialog(Review review) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Muokkaa arvostelua");

        TextField ratingEdit = new TextField("Arvosana (1-5)", String.valueOf(review.getRating()));
        TextArea commentEdit = new TextArea("Kommentti", review.getComment());

        Button save = new Button("Tallenna", e -> {
            try {
                int newRating = Integer.parseInt(ratingEdit.getValue());
                if (newRating < 1 || newRating > 5) {
                    Notification.show("Arvosanan tulee olla välillä 1–5.");
                    return;
                }
                review.setRating(newRating);
                review.setComment(commentEdit.getValue());
                reviewRepository.save(review);
                dialog.close();
                updateGrid(movieFilter.getValue());
                Notification.show("Arvostelu päivitetty.");
            } catch (NumberFormatException ex) {
                Notification.show("Virheellinen arvosana.");
            }
        });

        Button cancel = new Button("Peruuta", e -> dialog.close());

        FormLayout formLayout = new FormLayout(ratingEdit, commentEdit);
        dialog.add(formLayout, new HorizontalLayout(save, cancel));
        dialog.open();
    }

    private void updateGrid(Movie filterMovie) {
        List<Review> reviews;
        if (filterMovie != null) {
            reviews = reviewRepository.findByMovie(filterMovie);
        } else {
            reviews = reviewRepository.findAll();
        }
        reviewGrid.setItems(reviews);
    }

}
