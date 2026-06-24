package com.frameskipt.frameskipt.controller;

import com.frameskipt.frameskipt.model.Review;
import com.frameskipt.frameskipt.model.User;
import com.frameskipt.frameskipt.service.ReviewService;
import com.frameskipt.frameskipt.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProfileController {

    private final UserService userService;
    private final ReviewService reviewService;

    public ProfileController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        
        // 1. Fetch the real user entity using the logged-in username
        User user = userService.findByUsername(currentUser.getUsername());
        
        // 2. Fetch all reviews authored by this specific user
        List<Review> userReviews = reviewService.findByUserId(user.getId());

        // 3. Pass the data to the Thymeleaf template
        model.addAttribute("user", user);
        model.addAttribute("reviews", userReviews);

        return "profile";
    }
}