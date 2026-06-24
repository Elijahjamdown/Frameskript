package com.frameskipt.frameskipt.controller;

import com.frameskipt.frameskipt.model.Game;
import com.frameskipt.frameskipt.service.GameService;
import com.frameskipt.frameskipt.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class GameController {

    private final GameService gameService;
    private final ReviewService reviewService;

    public GameController(GameService gameService, ReviewService reviewService) {
        this.gameService = gameService;
        this.reviewService = reviewService;
    }

    /**
     * GET /games
     * The catalog: every game in the database, rendered as a grid.
     */
    @GetMapping("/games")
    public String showCatalog(Model model) {
        List<Game> games = gameService.findAll();
        model.addAttribute("games", games);
        return "games"; // resolves to templates/games.html
    }

    /**
     * GET /games/{id}
     * A single game's deep-dive page: full credits, genres, and
     * the calculated average rating pulled from ReviewService.
     */
    @GetMapping("/games/{id}")
    public String showGameDetail(@PathVariable Long id, Model model) {
        Game game = gameService.findById(id);
        Double averageRating = reviewService.getAverageRatingForGame(id);

        model.addAttribute("game", game);
        model.addAttribute("averageRating", averageRating);
        return "game-detail"; // resolves to templates/game-detail.html
    }
}