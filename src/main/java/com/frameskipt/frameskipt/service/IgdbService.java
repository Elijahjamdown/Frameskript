package com.frameskipt.frameskipt.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class IgdbService {

    @Value("${twitch.client-id}")
    private String clientId;

    @Value("${twitch.client-secret}")
    private String clientSecret;

    private String currentAccessToken;

    private final RestClient restClient;

    public IgdbService() {
        this.restClient = RestClient.create();
    }

    // ==========================================
    // AUTHENTICATION
    // ==========================================
    public void authenticateWithTwitch() {
        String authUrl = "https://id.twitch.tv/oauth2/token?client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&grant_type=client_credentials";
        try {
            Map response = restClient.post()
                    .uri(authUrl)
                    .retrieve()
                    .body(Map.class);
            if (response != null && response.containsKey("access_token")) {
                this.currentAccessToken = (String) response.get("access_token");
                System.out.println("✅ SUCCESSFULLY CONNECTED TO TWITCH IGDB!");
            }
        } catch (Exception e) {
            System.out.println("❌ FAILED TO CONNECT TO TWITCH: " + e.getMessage());
        }
    }

    // ==========================================
    // NEW RELEASES (for the carousel at the top)
    // Pulls games released in the last 30 days
    // with wide landscape artwork
    // ==========================================
    public List getNewReleases() {
        long thirtyDaysAgo = (System.currentTimeMillis() / 1000) - (30 * 24 * 60 * 60);
        long now = System.currentTimeMillis() / 1000;

        String body = "fields name, artworks.url, cover.url, first_release_date, summary;" +
                "where first_release_date >= " + thirtyDaysAgo +
                " & first_release_date <= " + now +
                " & artworks != null;" +
                "sort first_release_date desc;" +
                "limit 10;";

        return queryIgdb("https://api.igdb.com/v4/games", body);
    }

    // ==========================================
    // POPULAR THIS WEEK (activity based)
    // Pulls games with the most hype/follows
    // right now regardless of release date
    // ==========================================
    public List getPopularThisWeek() {
        String body = "fields name, cover.url, rating, hypes, follows;" +
                "where hypes != null & cover != null;" +
                "sort hypes desc;" +
                "limit 20;";

        return queryIgdb("https://api.igdb.com/v4/games", body);
    }

    // ==========================================
    // GENERAL FEED (activity + recency mix)
    // Rewards recent games but surfaces classics
    // if the community is actively logging them
    // ==========================================
    public List getGeneralFeed() {
        long twoYearsAgo = (System.currentTimeMillis() / 1000) - (2L * 365 * 24 * 60 * 60);

        String body = "fields name, cover.url, rating, hypes, follows, first_release_date;" +
                "where first_release_date >= " + twoYearsAgo +
                " & cover != null & rating != null;" +
                "sort follows desc;" +
                "limit 30;";

        return queryIgdb("https://api.igdb.com/v4/games", body);
    }

    // ==========================================
    // SHARED HELPER
    // All three methods above use this to
    // actually talk to the IGDB API
    // ==========================================
    private List queryIgdb(String url, String body) {
        try {
            return restClient.post()
                    .uri(url)
                    .header("Client-ID", clientId)
                    .header("Authorization", "Bearer " + currentAccessToken)
                    .header("Content-Type", "text/plain")
                    .body(body)
                    .retrieve()
                    .body(List.class);
        } catch (Exception e) {
            System.out.println("❌ IGDB QUERY FAILED: " + e.getMessage());
            return List.of();
        }
    }
}