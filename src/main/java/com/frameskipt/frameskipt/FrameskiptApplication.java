package com.frameskipt.frameskipt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.frameskipt.frameskipt.service.IgdbService;
@SpringBootApplication
public class FrameskiptApplication {
public static void main(String[] args) {
SpringApplication.run(FrameskiptApplication.class, args);
}
@Bean
public CommandLineRunner testTwitchConnection(IgdbService igdbService) {
return args -> {
System.out.println("Attempting to connect to Twitch...");
igdbService.authenticateWithTwitch();
};
}
}