package com.frameskipt.frameskipt.service;

import com.frameskipt.frameskipt.model.Game;
import com.frameskipt.frameskipt.repository.GameRepository;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public Game findById(Long id) {
        return gameRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "No game found with id: " + id
            ));
    }

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public List<Game> searchByTitle(String title) {
        return gameRepository.findByTitleContainingIgnoreCase(title);
    }
}