package com.frameskipt.frameskipt.service;
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
}
