package com.frameskipt.frameskipt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.frameskipt.frameskipt.model.Game;
import com.frameskipt.frameskipt.service.GameService;
import com.frameskipt.frameskipt.service.IgdbService;
import com.frameskipt.frameskipt.service.ReviewService;

@Controller
public class GameController {

    private final GameService gameService;
    private final ReviewService reviewService;
    private final IgdbService igdbService;

    public GameController(GameService gameService, ReviewService reviewService, IgdbService igdbService) {
        this.gameService = gameService;
        this.reviewService = reviewService;
        this.igdbService = igdbService;
    }

    @GetMapping("/games")
    public String showCatalog(Model model) {
        List<Game> games = gameService.findAll();
        model.addAttribute("games", games);

        // IGDB feeds for the home page sections
        model.addAttribute("newReleases", igdbService.getNewReleases());
        model.addAttribute("popularThisWeek", igdbService.getPopularThisWeek());
        model.addAttribute("generalFeed", igdbService.getGeneralFeed());

        return "games";
    }

    @GetMapping("/games/{id}")
    public String showGameDetail(@PathVariable Long id, Model model) {
        Game game = gameService.findById(id);
        Double averageRating = reviewService.getAverageRatingForGame(id);

        model.addAttribute("game", game);
        model.addAttribute("averageRating", averageRating);
        return "game-detail";
    }
}