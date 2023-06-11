package com.quizGpt.formManagement.Quiz.Service;

import com.quizGpt.formManagement.Quiz.Dto.CreateQuizRequestDto;

public interface ProducerService {
    void SendMessageToGptServer(CreateQuizRequestDto message);
    
}
