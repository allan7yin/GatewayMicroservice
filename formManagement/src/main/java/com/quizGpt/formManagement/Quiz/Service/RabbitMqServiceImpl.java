package com.quizGpt.formManagement.Quiz.Service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.modelmapper.ModelMapper;
import com.quizGpt.formManagement.Quiz.Dto.GptRequestDto;
import com.quizGpt.formManagement.Quiz.Dto.QuizDto;
import com.quizGpt.formManagement.Quiz.Entity.Quiz;
import com.quizGpt.formManagement.Quiz.Repository.QuizRepository;

public class RabbitMqServiceImpl implements ProducerService, ConsumerService{

    QuizRepository quizRepository;
    private RabbitTemplate rabbitTemplate;
    private String EXCHANGE_NAME = "form_management_rabbitmq_exchange";
    private String GPT_ROUTING_KEY = "gpt_rabbitmq_routing_key";
    private String AUTH_ROUTING_KEY = "auth_rabbitmq_routing_key";
    private String SENDER = "form_management_rabbitmq_sender";

    private final static String GPT_RESPONSE_QUEUE = "rabbitmq.gpt.response.queue";

    public RabbitMqServiceImpl(String EXCHANGE_NAME, String GPT_ROUTING_KEY, String AUTH_ROUTING_KEY, String SENDER, QuizRepository quizRepository) {
        this.EXCHANGE_NAME = EXCHANGE_NAME;
        this.GPT_ROUTING_KEY = GPT_ROUTING_KEY;
        this.AUTH_ROUTING_KEY = AUTH_ROUTING_KEY;
        this.SENDER = SENDER;
        this.quizRepository = quizRepository;
    }
    
    @Override
    public void SendMessageToAuthServer(String message) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, GPT_ROUTING_KEY, message);
    }

    @Override
    public void SendMessageToGptServer(GptRequestDto message) {
        message.setSender(SENDER);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, AUTH_ROUTING_KEY, message);
    }

    @Override
    @RabbitListener(queues = {GPT_RESPONSE_QUEUE})
    public void ConsumeMessageFromGptServer(QuizDto responseMessage) throws JsonProcessingException {
        // we want to save this to the database. Convert to different types to be able to do so. 
        ObjectMapper jsonToJavaObjectMapper = new ObjectMapper();
        ModelMapper dtoToJavaobjectMapper = new ModelMapper();

        // String json = jsonToJavaObjectMapper.writeValueAsString(responseMessage);
        Quiz quiz = dtoToJavaobjectMapper.map(responseMessage, Quiz.class);
        // Long quizId = quiz.getId();
        // json = jsonToJavaObjectMapper.writeValueAsString(quiz);
        quizRepository.save(quiz);
    }

}
