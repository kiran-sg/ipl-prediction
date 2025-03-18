package com.ipl.prediction.iplprediction.service;

import com.ipl.prediction.iplprediction.model.IplMatch;
import com.ipl.prediction.iplprediction.model.IplPlayer;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
public class CsvService {

    public List<IplMatch> readMatchesFromCsv() throws IOException {
        ClassPathResource resource = new ClassPathResource("ipl-2025-schedule.csv");
        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            HeaderColumnNameMappingStrategy<IplMatch> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(IplMatch.class);

            CsvToBean<IplMatch> csvToBean = new CsvToBeanBuilder<IplMatch>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        }
    }

    public List<IplPlayer> readPlayersFromCsv() throws IOException {
        ClassPathResource resource = new ClassPathResource("ipl-2025-players.csv");
        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            HeaderColumnNameMappingStrategy<IplPlayer> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(IplPlayer.class);

            CsvToBean<IplPlayer> csvToBean = new CsvToBeanBuilder<IplPlayer>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        }
    }
}
