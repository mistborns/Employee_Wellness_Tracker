package com.example.employeewellnesstracker.controller;

import com.example.employeewellnesstracker.model.Question;
import com.example.employeewellnesstracker.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/surveys/{surveyId}/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping
    public Question addQuestion(@PathVariable Long surveyId, @RequestBody Question question){
        return questionService.addQuestionToSurvey(surveyId, question);
    }

    @GetMapping
    public List<Question> getQuestions(@PathVariable Long surveyId){
        return questionService.getQuestionsBySurvey(surveyId);
    }

    @PutMapping("/{questionId}")
    public Question updateQuestion(@PathVariable Long questionId, @RequestBody Question question){
        return questionService.updateQuestion(questionId, question);
    }

    @DeleteMapping("/{questionId}")
    public void deleteQuestion(@PathVariable Long surveyId, @PathVariable Long questionId){
        questionService.deleteQuestion(surveyId, questionId);
    }

}
