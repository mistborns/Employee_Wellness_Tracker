package com.example.employeewellnesstracker.controller;

import com.example.employeewellnesstracker.dto.QuestionResponseDTO;
import com.example.employeewellnesstracker.dto.SurveyResponseBatchDTO;
import com.example.employeewellnesstracker.model.SurveyResponse;
import com.example.employeewellnesstracker.service.SurveyResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/responses")
public class SurveyResponseController {

    @Autowired
    private SurveyResponseService surveyResponseService;

    @PostMapping("/{surveyId}")
    public ResponseEntity<List<SurveyResponse>> submitResponses(
            @PathVariable Long surveyId,
            @RequestBody SurveyResponseBatchDTO responseBatchDTO) {
        return ResponseEntity.ok(surveyResponseService.submitResponses(responseBatchDTO));
    }

//    @GetMapping("/survey/{surveyId}")
//    public List<SurveyResponse> getResponsesBySurvey(@PathVariable Long surveyId ) {
//        return surveyResponseService.getResponsesBySurvey(surveyId);
//    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<SurveyResponseBatchDTO>> getResponsesGroupedBySurveys(@PathVariable Long employeeId) {
        List<SurveyResponseBatchDTO> surveyResponses = surveyResponseService.getResponsesGroupedBySurveys(employeeId);

        if (surveyResponses.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 if no responses found
        }

        return ResponseEntity.ok(surveyResponses);
    }

    @GetMapping("/survey/{surveyId}/employee/{employeeId}")
    public ResponseEntity<List<QuestionResponseDTO>> getResponsesForSurveyAndEmployee(
            @PathVariable Long surveyId,
            @PathVariable Long employeeId) {
        List<QuestionResponseDTO> responses = surveyResponseService.getResponsesForSurveyAndEmployee(surveyId, employeeId);

        if (responses.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 if no responses found
        }

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/survey/{surveyId}/employee/{employeeId}")
    public ResponseEntity<Void> deleteResponsesBySurveyAndEmployee(
            @PathVariable Long surveyId, @PathVariable Long employeeId) {

        surveyResponseService.deleteResponsesBySurveyAndEmployee(surveyId, employeeId);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }




}
