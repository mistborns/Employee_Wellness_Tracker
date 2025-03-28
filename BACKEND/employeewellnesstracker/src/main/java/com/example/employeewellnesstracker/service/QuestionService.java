package com.example.employeewellnesstracker.service;

import com.example.employeewellnesstracker.model.Question;
import com.example.employeewellnesstracker.model.Survey;
import com.example.employeewellnesstracker.repository.QuestionRepository;
import com.example.employeewellnesstracker.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    public Question addQuestionToSurvey(Long surveyId, Question question) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        question.setSurvey(survey);
        return questionRepository.save(question);
    }

    public List<Question> getQuestionsBySurvey(Long surveyId) {
        return questionRepository.findBySurveyId(surveyId);
    }

    public Question updateQuestion(Long id, Question updatedQuestion) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        question.setText(updatedQuestion.getText());
        return questionRepository.save(question);
    }

    public void deleteQuestion(Long surveyId, Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        if (!Objects.equals(question.getSurvey().getId(), surveyId)) {
            throw new RuntimeException("Question does not belong to this survey");
        }
        questionRepository.deleteById(questionId);
    }
}
