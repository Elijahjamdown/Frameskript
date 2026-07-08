package com.frameskipt.frameskipt.controller;

import com.frameskipt.frameskipt.service.IgdbService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/igdb")
public class IgdbController {

    private final IgdbService igdbService;

    public IgdbController(IgdbService igdbService) {
        this.igdbService = igdbService;
    }

    // Powers the carousel at the top
    @GetMapping("/new-releases")
    public List getNewReleases() {
        return igdbService.getNewReleases();
    }

    // Powers the Popular This Week row
    @GetMapping("/popular")
    public List getPopularThisWeek() {
        return igdbService.getPopularThisWeek();
    }

    // Powers the general feed at the bottom
    @GetMapping("/feed")
    public List getGeneralFeed() {
        return igdbService.getGeneralFeed();
    }
}