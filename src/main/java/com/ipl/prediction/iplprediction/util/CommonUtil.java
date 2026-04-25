package com.ipl.prediction.iplprediction.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CommonUtil {

    private static String tournamentPredictionCutoff;

    @Value("${tournament.prediction.cutoff}")
    public void setTournamentPredictionCutoff(String cutoff) {
        tournamentPredictionCutoff = cutoff;
    }

    public static boolean isPredictionAllowed(String matchDateTime) {
        LocalDateTime matchTime;
        if (matchDateTime.contains("T")) {
            matchTime = OffsetDateTime.parse(matchDateTime).toLocalDateTime();
        } else {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            matchTime = LocalDateTime.parse(matchDateTime, inputFormatter);
        }
        LocalDateTime oneHourBeforeMatch = matchTime.minusHours(1);
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.isBefore(oneHourBeforeMatch);
    }

    public static boolean isTournamentPredictionAllowed() {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        LocalDateTime cutoffLocal = LocalDateTime.parse(tournamentPredictionCutoff);
        ZonedDateTime cutoffTime = cutoffLocal.atZone(ZoneId.of("Asia/Kolkata"));
        return currentTime.isBefore(cutoffTime);
    }
}
