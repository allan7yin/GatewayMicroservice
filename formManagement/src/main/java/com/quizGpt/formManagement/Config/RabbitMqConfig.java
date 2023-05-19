package com.quizGpt.formManagement.Config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    // Routing Keys 
    @Value("{gpt.rabbitmq.routing.key}")
    private String GPT_ROUTING_KEY;

    @Value("${rabbitmq.auth.login.queue.routing.key}")
    private String LOGIN_QUEUE_ROUTING_KEY;

    @Value("${rabbitmq.auth.signup.queue.routing.key}")
    private String SIGN_UP_QUEUE_ROUTING_KEY;
    
    // queues being read from 

    @Value("{rabbitmq.gpt.response.queue}")
    private String GPT_RESPONSE_QUEUE;

    @Value("${rabbitmq.auth.login.queue}")
    private String AUTH_LOGIN_RESPONSE_QUEUE;

    @Value("${rabbitmq.auth.signup.queue}")
    private String AUTH_SIGNUP_RESPONSE_QUEUE;

    // exchanges 

    @Value("{form.management.rabbitmq.exchange}")
    private String GPT_EXCHANGE;

    @Value("${rabbitmq.auth.exchange}")
    private String AUTH_EXCHANGE;

    // Queues 
    @Bean
    public Queue GptResponseQueue(){
        return new Queue(GPT_RESPONSE_QUEUE, false);
    }
    @Bean
    public Queue AuthLoginResponseQueue(){
        return new Queue(AUTH_LOGIN_RESPONSE_QUEUE, true);
    }
    @Bean
    public Queue AuthSignupResponseQueue(){
        return new Queue(AUTH_SIGNUP_RESPONSE_QUEUE, true);
    }

    // we are making it so there is one route key per "path", so we set this up as a direct exchange 
    @Bean
    public DirectExchange GptExchange(){
        return new DirectExchange(GPT_EXCHANGE);
    }
    @Bean
    public DirectExchange AuthExchange(){
        return new DirectExchange(AUTH_EXCHANGE);
    }

    // bind the queues to the exchanges 

    @Bean
    public Binding GptBinding(){
        return BindingBuilder
                .bind(GptExchange())
                .to(GptExchange())
                .with(GPT_ROUTING_KEY);
    }

    @Bean
    public Binding LoginBinding(){
        return BindingBuilder
                .bind(AuthLoginResponseQueue())
                .to(AuthExchange())
                .with(LOGIN_QUEUE_ROUTING_KEY);
    }

    @Bean
    public Binding SignUpBinding(){
        return BindingBuilder
                .bind(AuthSignupResponseQueue())
                .to(AuthExchange())
                .with(SIGN_UP_QUEUE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
