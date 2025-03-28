package com.example.employeewellnesstracker.repository;

import com.example.employeewellnesstracker.model.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {
    List<SurveyResponse> findBySurveyId(Long surveyId);
    List<SurveyResponse> findByEmployeeId(Long employeeId);
    List<SurveyResponse> findBySurveyIdAndEmployeeId(Long surveyId, Long employeeId);
}
