package com.viladafolha.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class RabbitMQConfig {

//    @Bean
//    public MessageConverter getMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }

    @Value("${queue.name.1}")
    private String queueName1;
    @Value("${queue.name.2}")
    private String queueName2;
    @Value("${exchange.name}")
    private String exchange;



    @Bean
    public Queue queue1() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 300);
        return new Queue(queueName1,true, false, false, args);
    }

    @Bean
    public Queue queue2() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 300);

        return new Queue(queueName2, true, false, false, args);
    }

    @Bean
    public DirectExchange getDirectExchange() {
        return new DirectExchange(exchange, true, false);
    }

    @Bean
    public Binding binding1() {
        Map<String, Object> args = new HashMap<>();
        return new Binding(queueName1, Binding.DestinationType.QUEUE, exchange, queueName1, args);
    }

    @Bean
    public Binding binding2() {
        Map<String, Object> args = new HashMap<>();
        return new Binding(queueName2, Binding.DestinationType.QUEUE, exchange, queueName2, args);
    }
}
