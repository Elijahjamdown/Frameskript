package com.frameskipt.frameskipt.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- The Two Foreign Keys ---
    // @ManyToOne is the "many" side. Many reviews can belong to
    // one User. JPA will create a "user_id" column in the reviews
    // table that is a foreign key pointing to the users table.
    //
    // @JoinColumn(name = "user_id") lets you name that FK column
    // explicitly, which keeps your schema readable.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    // --- The Critique ---
    // @Lob (Large Object) signals to JPA that this field should
    // be mapped to a large text column type (TEXT in PostgreSQL,
    // CLOB in H2). A standard VARCHAR has a 255-char default limit.
    // A deep critical essay needs far more than that.

    @Lob
    @Column(nullable = false)
    private String body;

    // --- The Half-Star Rating ---
    // Stored as a Double. 4.5 means 4.5 stars. No conversion needed,
    // ever. We'll add @Min/@Max validation in a later phase when we
    // wire in the service layer.

    @Column(nullable = false)
    private Double rating;

    // --- The Timestamp ---
    // LocalDateTime is the modern Java way. We'll use @PrePersist
    // to auto-set this when a Review is first saved, so the
    // controller never has to think about it.

    @Column(name = "logged_at", nullable = false)
    private LocalDateTime loggedAt;

    @PrePersist
    protected void onLog() {
        this.loggedAt = LocalDateTime.now();
    }

    // --- Constructors ---

    public Review() {}

    public Review(User user, Game game, String body, Double rating) {
        this.user = user;
        this.game = game;
        this.body = body;
        this.rating = rating;
    }

    // --- Getters & Setters ---

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public LocalDateTime getLoggedAt() { return loggedAt; }
}