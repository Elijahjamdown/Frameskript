package com.frameskipt.frameskipt.repository;

import com.frameskipt.frameskipt.model.Game;
import com.frameskipt.frameskipt.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByTitleContainingIgnoreCase(String title);
    List<Game> findByGenresContaining(Genre genre);
    List<Game> findByGameDirectorIgnoreCase(String gameDirector);
}