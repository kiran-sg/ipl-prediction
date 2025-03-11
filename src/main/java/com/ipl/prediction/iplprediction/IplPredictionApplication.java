package com.ipl.prediction.iplprediction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/predict")
public class IplPredictionApplication {

    public static void main(String[] args) {
        SpringApplication.run(IplPredictionApplication.class, args);
    }

    @GetMapping
    public Map<String, String> predict(@RequestParam String team1, @RequestParam String team2) {
        String winner = Math.random() > 0.5 ? team1 : team2;
        return Map.of("team1", team1, "team2", team2, "predicted_winner", winner);
    }
}
