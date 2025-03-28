package com.example.employeewellnesstracker.service;


import com.example.employeewellnesstracker.model.Survey;
import com.example.employeewellnesstracker.repository.SurveyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;

    public SurveyService(SurveyRepository surveyRepository){
        this.surveyRepository = surveyRepository;
    }

    //create a survey
    public Survey createSurvey(Survey survey){
        return surveyRepository.save(survey);
    }

    //get all surveys
    public List<Survey> getAllSurveys(){
        return surveyRepository.findAll();
    }
    //get survey by id
    public Survey getSurveyById(Long id){
        return surveyRepository.findById(id).orElseThrow(() -> new RuntimeException("could not find survey"));
    }

    //update existing surveys
    public Survey updateSurvey( Long id,  Survey updatedSurvey){
       Survey survey =  surveyRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Survey not found"));
       survey.setTitle(updatedSurvey.getTitle());
       survey.setIsActive(updatedSurvey.getIsActive());
       survey.setDescription(updatedSurvey.getDescription());

       return surveyRepository.save(survey);
    }

    //delete a survey
    public void deleteSurvey(Long id){
        surveyRepository.deleteById(id);
    }

}
