package com.quizGpt.formManagement.Account.Service;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizGpt.formManagement.Account.Entity.MqResponse;
import com.quizGpt.formManagement.Account.Repository.MqResponseRepository;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
public class AuthMqServiceImpl implements AuthMqService {

    // establish RabbitMQ connection and broadcast message to one of the queues 

    private RabbitTemplate rabbitTemplate;
    private final static String AUTH_EXCHANGE = "authExchange";
    private final static String LOGIN_QUEUE = "loginQueue";
    private final static String SIGN_UP_QUEUE = "signUpQueue";
    private ObjectMapper jsonMapper;
    private final MqResponseRepository mqResponseRepository;


    public AuthMqServiceImpl(RabbitTemplate rabbitTemplate, ObjectMapper jsonMapper, MqResponseRepository mqResponseRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.jsonMapper = jsonMapper;
        this.mqResponseRepository = mqResponseRepository;
    }

    @Override
    public <T> String SendLoginRequestDto(T loginRequestMessage) throws JsonProcessingException {
        return SendMessageToQueue(LOGIN_QUEUE, loginRequestMessage);
    }

    @Override
    public <T> String SendSignUpRequestDto(T SignUpRequestMessage) throws JsonProcessingException {
        return SendMessageToQueue(LOGIN_QUEUE, SignUpRequestMessage);
    }

    @Override
    @RabbitListener(queues = LOGIN_QUEUE)
    public void ConsumeLoginMessageFromMQ(Object incomingMessage) {
        try {
            save(incomingMessage); // need to cast this to a Message type 
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    @Override
    @RabbitListener(queues = SIGN_UP_QUEUE)
    public void ConsumeSignUpMessageFromMQ(Object incomingMessage) {
        try {
            save(incomingMessage);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void save(Object incomingMessage) throws UnsupportedEncodingException {
        // first, cast this into a message type 
        Message message = (Message) incomingMessage;
        String correlationID = message.getMessageProperties().getCorrelationId();
        
        String JSON = new String(message.getBody(), "UTF-8");

        try {
            mqResponseRepository.save( new MqResponse(correlationID, JSON));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private <T> String SendMessageToQueue(String routingKey, T message) throws JsonProcessingException {
        // this method will deal with 2 queues, login queue and sign-up queue 
        // using direct queue, will need routing key to divert messages to respective destination queues 
        String outputMessage;
        try {
            outputMessage = jsonMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

        String correlationId = UUID.randomUUID().toString();
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setCorrelationId(correlationId);
        Message deliverable = new Message(outputMessage.getBytes(), messageProperties);
        // effectively, we are attatching correlationID to the message, and converting to binary array format - neccessary for network protocols such as RabbitMQ
        
        rabbitTemplate.convertAndSend(AUTH_EXCHANGE, routingKey, deliverable);
        return correlationId;
    }

}
