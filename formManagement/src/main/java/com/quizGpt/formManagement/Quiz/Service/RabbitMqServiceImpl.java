package com.quizGpt.formManagement.Quiz.Service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.modelmapper.ModelMapper;

import com.quizGpt.formManagement.Quiz.Dto.CreateQuizRequestDto;
import com.quizGpt.formManagement.Quiz.Dto.GptRequestDto;
import com.quizGpt.formManagement.Quiz.Dto.QuizDto;
import com.quizGpt.formManagement.Quiz.Entity.Quiz;
import com.quizGpt.formManagement.Quiz.Repository.QuizRepository;

import org.springframework.beans.factory.annotation.Value;

public class RabbitMqServiceImpl implements ProducerService, ConsumerService{

    QuizRepository quizRepository;

    private RabbitTemplate rabbitTemplate;

    @Value("{rabbitmq.gpt.exchange}")
    private String GPT_EXCHANGE;
    
    @Value("{gpt.request.rabbitmq.routing.key}")
    private String GPT_REQUEST_ROUTING_KEY;

    // @Value("{form.management.rabbitmq.sender}")
    // private String SENDER;

    public RabbitMqServiceImpl(String GPT_EXCHANGE, String GPT_ROUTING_KEY, String AUTH_ROUTING_KEY, QuizRepository quizRepository, RabbitTemplate rabbitTemplate) {
        this.GPT_EXCHANGE = GPT_EXCHANGE;
        this.GPT_REQUEST_ROUTING_KEY = GPT_ROUTING_KEY;
        this.GPT_EXCHANGE = AUTH_ROUTING_KEY;
        // this.SENDER = SENDER;
        this.quizRepository = quizRepository;
        this.rabbitTemplate = rabbitTemplate;
    }
    
    @Override
    public void SendMessageToGptServer(CreateQuizRequestDto message) {
        rabbitTemplate.convertAndSend(GPT_EXCHANGE, GPT_REQUEST_ROUTING_KEY, message);
    }

    //// NEED TO FIX THIS
    @Override
    @RabbitListener(queues = {"{rabbitmq.gpt.response.queue}"})
    public void ConsumeMessageFromGptServer(QuizDto responseMessage) throws JsonProcessingException {
        // we want to save this to the database. Convert to different types to be able to do so. 
        ModelMapper dtoToJavaobjectMapper = new ModelMapper();

        // String json = jsonToJavaObjectMapper.writeValueAsString(responseMessage);
        Quiz quiz = dtoToJavaobjectMapper.map(responseMessage, Quiz.class);
        // Long quizId = quiz.getId();
        // json = jsonToJavaObjectMapper.writeValueAsString(quiz);
        quizRepository.save(quiz);
    }
}
