package com.example.employeewellnesstracker.service;

import com.example.employeewellnesstracker.dto.QuestionResponseDTO;
import com.example.employeewellnesstracker.dto.SurveyResponseBatchDTO;
import com.example.employeewellnesstracker.model.*;
import com.example.employeewellnesstracker.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SurveyResponseService {

    @Autowired
    private SurveyResponseRepository surveyResponseRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SurveyRepository surveyRepository;


    public List<SurveyResponse> submitResponses(SurveyResponseBatchDTO responseBatchDTO) {
        List<SurveyResponse> responseList = new ArrayList<>();

        // Fetch the Employee entity
        Employee employee = employeeRepository.findById(responseBatchDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Fetch the Survey entity
        Survey survey = surveyRepository.findById(responseBatchDTO.getSurveyId())
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        for (QuestionResponseDTO questionResponse : responseBatchDTO.getResponseDTOList()) {
            // Fetch the Question entity
            Question question = questionRepository.findById(questionResponse.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            SurveyResponse surveyResponse = new SurveyResponse();
            surveyResponse.setSurvey(survey);
            surveyResponse.setEmployee(employee);
            surveyResponse.setQuestion(question);
            surveyResponse.setResponse(questionResponse.getResponse());

            responseList.add(surveyResponseRepository.save(surveyResponse));
        }
        return responseList;
    }

    // Get all responses for a survey
    public List<SurveyResponse> getResponsesBySurvey(Long surveyId) {
        return surveyResponseRepository.findBySurveyId(surveyId);
    }

    public List<SurveyResponseBatchDTO> getResponsesGroupedBySurveys(Long employeeId) {
        // Fetch all responses for the employee
        List<SurveyResponse> surveyResponses = surveyResponseRepository.findByEmployeeId(employeeId);

        // Group responses by surveyId
        Map<Long, SurveyResponseBatchDTO> surveyResponseMap = new HashMap<>();

        for (SurveyResponse response : surveyResponses) {
            Long surveyId = response.getSurvey().getId();

            // Create a new BatchResponseDTO if it doesn't exist for the survey
            surveyResponseMap.putIfAbsent(surveyId, new SurveyResponseBatchDTO());
            SurveyResponseBatchDTO batchDTO = surveyResponseMap.get(surveyId);

            // Populate survey title and description (only once per survey)
            batchDTO.setSurveyId(surveyId);
            batchDTO.setSurveyTitle(response.getSurvey().getTitle());
            batchDTO.setSurveyDescription(response.getSurvey().getDescription());

            // Map the question response to QuestionResponseDTO
            QuestionResponseDTO questionDTO = new QuestionResponseDTO();
            questionDTO.setQuestionId(response.getQuestion().getId());
            questionDTO.setQuestionText(response.getQuestion().getText());
            questionDTO.setResponse(response.getResponse());

            if (batchDTO.getResponseDTOList() == null) {
                batchDTO.setResponseDTOList(new ArrayList<>());
            }
            batchDTO.getResponseDTOList().add(questionDTO);
        }

        return new ArrayList<>(surveyResponseMap.values());
    }

    public List<QuestionResponseDTO> getResponsesForSurveyAndEmployee(Long surveyId, Long employeeId) {
        // Fetch all responses for the given survey and employee
        List<SurveyResponse> surveyResponses = surveyResponseRepository.findBySurveyIdAndEmployeeId(surveyId, employeeId);

        // Map responses to DTOs
        return surveyResponses.stream()
                .map(response -> {
                    QuestionResponseDTO dto = new QuestionResponseDTO();
                    dto.setQuestionId(response.getQuestion().getId());
                    dto.setQuestionText(response.getQuestion().getText());
                    dto.setResponse(response.getResponse());
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
