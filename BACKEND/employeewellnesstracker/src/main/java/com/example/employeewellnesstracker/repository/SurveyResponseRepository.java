package com.example.employeewellnesstracker.repository;

import com.example.employeewellnesstracker.model.SurveyResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {
    List<SurveyResponse> findBySurveyId(Long surveyId);
    List<SurveyResponse> findByEmployeeId(Long employeeId);
    List<SurveyResponse> findBySurveyIdAndEmployeeId(Long surveyId, Long employeeId);

    @Modifying
    @Query("DELETE FROM SurveyResponse sr WHERE sr.survey.id = :surveyId AND sr.employee.id = :employeeId")
    void deleteBySurveyIdAndEmployeeId(@Param("surveyId") Long surveyId, @Param("employeeId") Long employeeId);


}
