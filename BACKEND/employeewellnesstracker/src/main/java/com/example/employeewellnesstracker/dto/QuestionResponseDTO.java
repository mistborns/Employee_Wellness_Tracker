package com.example.employeewellnesstracker.dto;

import lombok.Data;

@Data
public class QuestionResponseDTO {

    private Long questionId;
    private String response;
    private String questionText; //field used for fetching(get req)  not for submission
    private Long responseId;
}
