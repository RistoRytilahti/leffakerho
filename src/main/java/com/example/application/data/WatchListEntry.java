package com.example.application.data;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class WatchListEntry extends AbstractEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private Movie movie;

    private LocalDateTime addedAt = LocalDateTime.now();

    // getterit ja setterit


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}
