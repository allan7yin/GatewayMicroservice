package com.quizGpt.formManagement.Quiz.Controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizGpt.formManagement.Quiz.Dto.CreateQuizRequestDto;
import com.quizGpt.formManagement.Quiz.Dto.QuizDto;
import com.quizGpt.formManagement.Quiz.Entity.Quiz;
import com.quizGpt.formManagement.Quiz.Exception.QuizNotFoundException;
import com.quizGpt.formManagement.Quiz.Service.QuizServiceImpl;
import com.quizGpt.formManagement.Quiz.Service.RabbitMqServiceImpl;

@RestController
@RequestMapping("/api/v1")
public class QuizController {

    private RabbitMqServiceImpl rabbitMqService;
    private ModelMapper modelMapper;
    private QuizServiceImpl quizService;    

    // @RabbitListener
    @PostMapping("/quiz")
    public void CreateQuiz(@RequestBody CreateQuizRequestDto createQuizRequestDto) {
        // quizDto has topic, number of questions, number of options per question, difficutly. We will package all of this as a JSON and send it to the RabbitMQ queue 
        // we will send this as a request to queue. in the queue listener, we will recive the quizDto and save it to the database 
        rabbitMqService.SendMessageToGptServer(createQuizRequestDto);
    }

    @GetMapping("/quizzes")
    public List<QuizDto> GetAllQuizzes() {
        List<QuizDto> quizDtos = new ArrayList<>();
        List<Quiz> quizzes = this.quizService.GetAllQuizzes();

        for (Quiz quiz : quizzes) {
            QuizDto quizDto = modelMapper.map(quiz, QuizDto.class);
            quizDtos.add(quizDto);
        }

        return quizDtos;
    }

    @GetMapping("/quiz/{id}")
    public QuizDto GetQuiz(@PathVariable Long id) throws QuizNotFoundException {
        Quiz quiz =  quizService.GetQuizById(id);
        var quizDto = modelMapper.map(quiz, QuizDto.class);
        return quizDto;
    }

    @DeleteMapping("/quiz/{id}")
    public void DeleteQuiz(@PathVariable Long id) throws QuizNotFoundException {
        quizService.DeleteQuiz(id);
    }
    
}
