package com.ipl.prediction.iplprediction.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CommonUtil {

    public static boolean isPredictionAllowed(String matchDateTime) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime matchTime = LocalDateTime.parse(matchDateTime, inputFormatter);
        LocalDateTime oneHourBeforeMatch = matchTime.minusHours(1);
        LocalDateTime currentTime = LocalDateTime.now();

        return !(currentTime.toLocalDate().isEqual(matchTime.toLocalDate())
                && currentTime.isAfter(oneHourBeforeMatch));
    }

    public static boolean isTournamentPredictionAllowed() {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        ZonedDateTime cutoffTime = ZonedDateTime.of(2025, 5, 23, 12, 0, 0, 0, ZoneId.of("Asia/Kolkata"));
        return currentTime.isBefore(cutoffTime);
    }
}
