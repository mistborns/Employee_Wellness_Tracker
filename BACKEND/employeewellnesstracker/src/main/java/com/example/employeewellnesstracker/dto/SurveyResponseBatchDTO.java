package com.example.employeewellnesstracker.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;


@Data
public class SurveyResponseBatchDTO {
    private Long employeeId;
    private Long surveyId;
    private List<QuestionResponseDTO> responseDTOList;
    private String surveyTitle; // field used only while fetching data not for submission
    private String surveyDescription;// field used only while fetching data not for submission
}
