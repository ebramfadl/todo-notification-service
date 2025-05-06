package com.todo.notificationservice.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String USER_QUEUE = "user_queue";
    public static final String USER_EXCHANGE  = "user_exchange";
    public static final String USER_ROUTING_KEY  = "user_routing_key";

    public static final String TASK_QUEUE = "task_queue";
    public static final String TASK_EXCHANGE  = "task_exchange";
    public static final String TASK_ROUTING_KEY  = "task_routing_key";

    @Bean
    public Queue userQueue() {
        return new Queue(USER_QUEUE);
    }

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    @Bean
    public Binding userBinding(Queue userQueue, TopicExchange userExchange) {
        return BindingBuilder
                .bind(userQueue)
                .to(userExchange)
                .with(USER_ROUTING_KEY);
    }

    @Bean
    public Queue taskQueue() {
        return new Queue(TASK_QUEUE);
    }

    @Bean
    public TopicExchange taskExchange() {
        return new TopicExchange(TASK_EXCHANGE);
    }

    @Bean
    public Binding taskBinding(Queue taskQueue, TopicExchange taskExchange) {
        return BindingBuilder
                .bind(taskQueue)
                .to(taskExchange)
                .with(TASK_ROUTING_KEY);
    }
}
