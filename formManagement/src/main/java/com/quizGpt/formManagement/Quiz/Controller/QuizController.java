package com.quizGpt.formManagement.Quiz.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizGpt.formManagement.Quiz.Dto.QuizDto;
import com.quizGpt.formManagement.Quiz.Entity.Quiz;

@RestController
@RequestMapping("/api/v1")
public class QuizController {

    private IQuizService quizService;

    @GetMapping("/quizzes")
    public List<QuizDto> GetAllQuizzes() {
        List<QuizDto> quizzes = new ArrayList<QuizDto>;
        List<Quiz> quizzes = this.quizService.GetAllQuizzes();

        for (Quiz quiz : quizzes) {
            QuizDto quizDto = modelMapper.map(quiz, QuizDto.class);
            quizDtos.add(quizDto);
        }

        return quizDtos;
    }
    
}
