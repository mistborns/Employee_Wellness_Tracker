package com.example.employeewellnesstracker.controller;


import com.example.employeewellnesstracker.model.Survey;
import com.example.employeewellnesstracker.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/surveys")
public class SurveyController {

    @Autowired
    SurveyService surveyService;

    @PostMapping
    public Survey createSurvey(@RequestBody Survey survey){
        return surveyService.createSurvey(survey);
    }

    //get all surveys
    @GetMapping
    public List<Survey> getAllSurveys(){
        return surveyService.getAllSurveys();
    }

    //survey by id
    @GetMapping("/{id}")
    public Survey getSurveyById(@PathVariable Long id){
        return surveyService.getSurveyById(id);
    }

    @PutMapping("/{id}")
    public Survey updateSurvey(@PathVariable Long id, @RequestBody Survey survey){
        return surveyService.updateSurvey(id, survey);
    }

    @DeleteMapping("/{id}")
    public void deleteSurvey(@PathVariable Long id){
        surveyService.deleteSurvey(id);
    }





}
