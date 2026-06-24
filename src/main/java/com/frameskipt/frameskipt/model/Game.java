package com.frameskipt.frameskipt.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Core Identity ---

    @Column(nullable = false)
    private String title;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "cover_art_url")
    private String coverArtUrl;

    // --- The Cinematic Credits (Cast & Crew) ---
    // Each field is a named person, not a corporate entity.
    // This is what separates Frameskipt from a generic database.

    private String studio;          // e.g., "FromSoftware"
    private String publisher;       // e.g., "Bandai Namco"

    @Column(name = "game_director")
    private String gameDirector;    // e.g., "Hidetaka Miyazaki"

    @Column(name = "lead_writer")
    private String leadWriter;      // e.g., "Yoko Taro"

    @Column(name = "lead_composer")
    private String leadComposer;    // e.g., "Yoko Shimomura"

    @Column(name = "art_director")
    private String artDirector;     // e.g., "Kenichiro Yoshida"

    @Column(name = "lead_programmer")
    private String leadProgrammer;

    // --- Granular Genres ---
    // @ElementCollection tells JPA that this list of Enums isn't
    // a separate @Entity, but it still needs its own table.
    // JPA will auto-create a "game_genres" join table with
    // two columns: game_id and genre.
    //
    // @Enumerated(STRING) stores the word "SOULSLIKE", not the
    // integer index 10. This means your database is human-readable
    // and won't silently corrupt if you ever reorder the enum.

    @ElementCollection(targetClass = Genre.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "game_genres", joinColumns = @JoinColumn(name = "game_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private List<Genre> genres = new ArrayList<>();

    // --- Relationships ---
    // mappedBy = "game" tells JPA: "The 'game' field on the Review
    // class is the one that owns and manages this relationship."
    // This side is read-only for relationship management.
    // CascadeType.ALL means if a Game is deleted, its Reviews die with it.
    // orphanRemoval = true cleans up any Review that gets detached.

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // --- Constructors ---

    public Game() {}

    public Game(String title, Integer releaseYear, String coverArtUrl) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.coverArtUrl = coverArtUrl;
    }

    // --- Getters & Setters ---

    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public String getCoverArtUrl() { return coverArtUrl; }
    public void setCoverArtUrl(String coverArtUrl) { this.coverArtUrl = coverArtUrl; }

    public String getStudio() { return studio; }
    public void setStudio(String studio) { this.studio = studio; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getGameDirector() { return gameDirector; }
    public void setGameDirector(String gameDirector) { this.gameDirector = gameDirector; }

    public String getLeadWriter() { return leadWriter; }
    public void setLeadWriter(String leadWriter) { this.leadWriter = leadWriter; }

    public String getLeadComposer() { return leadComposer; }
    public void setLeadComposer(String leadComposer) { this.leadComposer = leadComposer; }

    public String getArtDirector() { return artDirector; }
    public void setArtDirector(String artDirector) { this.artDirector = artDirector; }

    public String getLeadProgrammer() { return leadProgrammer; }
    public void setLeadProgrammer(String leadProgrammer) { this.leadProgrammer = leadProgrammer; }

    public List<Genre> getGenres() { return genres; }
    public void setGenres(List<Genre> genres) { this.genres = genres; }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }
}