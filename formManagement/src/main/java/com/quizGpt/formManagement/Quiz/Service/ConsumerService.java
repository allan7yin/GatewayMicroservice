package com.quizGpt.formManagement.Quiz.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.quizGpt.formManagement.Quiz.Dto.QuizDto;

public interface ConsumerService {
    public void ConsumeMessageFromGptServer(QuizDto message) throws JsonProcessingException;
}
