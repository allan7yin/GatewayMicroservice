package com.quizGpt.formManagement.Quiz.Service;

import com.quizGpt.formManagement.Quiz.Dto.GptRequestDto;

public interface ProducerService {
    void SendMessageToAuthServer(String message);

    void SendMessageToGptServer(GptRequestDto message);
    
}
