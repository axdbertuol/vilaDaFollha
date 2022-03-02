package com.viladafolha.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class RabbitMQConfig {

    @Bean
    public MessageConverter getMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue queue1() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 300);

        return new Queue("com.sysmsg.viladafolha", true, false, false, args);
    }

    @Bean
    public Queue queue2() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 300);

        return new Queue("com.print.viladafolha", true, false, false, args);
    }

    @Bean
    public DirectExchange getDirectExchange() {
        return new DirectExchange("com.direct.viladafolha", true, false);
    }

    @Bean
    public Binding binding1() {
        Map<String, Object> args = new HashMap<>();
        return new Binding("com.sysmsg.viladafolha", Binding.DestinationType.QUEUE, "com.direct.viladafolha", "com.sysmsg.viladafolha", args);
    }

    @Bean
    public Binding binding2() {
        Map<String, Object> args = new HashMap<>();
        return new Binding("com.print.viladafolha2", Binding.DestinationType.QUEUE, "com.direct.viladafolha", "com.print.viladafolha", args);
    }
}
