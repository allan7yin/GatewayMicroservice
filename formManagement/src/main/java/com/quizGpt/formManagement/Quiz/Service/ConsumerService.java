package com.quizGpt.formManagement.Quiz.Service;

import com.quizGpt.formManagement.Quiz.Dto.GptResponseDto;

public interface ConsumerService {
    public void ConsumeMessageFromGptServer(GptResponseDto message);
}
