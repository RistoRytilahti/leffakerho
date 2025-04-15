package com.example.application.data;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Movie extends AbstractEntity {

    private String title;
    private String director;
    private int releaseYear;
    private String genre;
    private String description;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // @ManyToMany käyttäjiin
    @ManyToMany(mappedBy = "ownMovies")  // Tämä määrittelee suhteen 'ownMovies' käyttäjässä
    private Set<User> usersWithInOwnMovies = new HashSet<>(); // Käyttäjät, joilla on tämä elokuva


    // Getterit ja setterit...

    public Set<User> getUsersWithInOwnMovies() {
        return usersWithInOwnMovies;
    }

    public void setUsersWithInOwnMovies(Set<User> usersWithInOwnMovies) {
        this.usersWithInOwnMovies = usersWithInOwnMovies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

}