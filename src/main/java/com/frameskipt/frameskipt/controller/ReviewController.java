package com.frameskipt.frameskipt.controller;

import com.frameskipt.frameskipt.model.Game;
import com.frameskipt.frameskipt.model.Review;
import com.frameskipt.frameskipt.model.User;
import com.frameskipt.frameskipt.service.GameService;
import com.frameskipt.frameskipt.service.ReviewService;
import com.frameskipt.frameskipt.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final GameService gameService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService,
                             GameService gameService,
                             UserService userService) {
        this.reviewService = reviewService;
        this.gameService = gameService;
        this.userService = userService;
    }

    @PostMapping("/games/{id}/reviews")
    public String submitReview(@PathVariable Long id,
                                @RequestParam String body,
                                @RequestParam Double rating,
                                @AuthenticationPrincipal UserDetails currentUser,
                                RedirectAttributes redirectAttributes) {

        // @AuthenticationPrincipal injects the UserDetails object
        // Spring Security stored in the SecurityContext at login time —
        // the same object CustomUserDetailsService.loadUserByUsername()
        // returned. We pull the username from it, then fetch the full
        // User entity so we have the database ID and all other fields.
        User user = userService.findByUsername(currentUser.getUsername());
        Game game = gameService.findById(id);

        Review review = new Review(user, game, body, rating);

        try {
            reviewService.saveReview(review);
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("reviewError", ex.getMessage());
        }

        return "redirect:/games/" + id;
    }
}