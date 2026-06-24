package com.frameskipt.frameskipt.controller;

import com.frameskipt.frameskipt.model.User;
import com.frameskipt.frameskipt.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /register
     * Shows a blank registration form. We MUST put an empty User
     * into the Model here, because th:object="${user}" in the
     * template requires SOMETHING to bind to, even on first load.
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    /**
     * POST /register
     * Spring populates a User object from the submitted form fields
     * automatically, via @ModelAttribute, before this method runs.
     */
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute User user, Model model) {
        try {
            userService.registerUser(user);
            return "redirect:/games"; // success → bounce to the catalog
        } catch (IllegalArgumentException ex) {
            // Re-add the user so the form retains whatever the
            // person already typed, instead of clearing the form.
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", ex.getMessage());
            return "register"; // re-render the SAME page, now with an error
        }
    }
}