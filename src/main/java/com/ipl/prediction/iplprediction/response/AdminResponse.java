package com.ipl.prediction.iplprediction.response;

import com.ipl.prediction.iplprediction.dto.MatchResultDto;
import com.ipl.prediction.iplprediction.dto.PredictionDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminResponse {
    private String message;
    private boolean status;
    private List<PredictionDto> predictions;
    private MatchResultDto matchResult;
}
