package com.bjpowernode.config;

import com.bjpowernode.consts.QueueConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;

@Configuration
public class RabbitMqConfig {
    @Bean
    public Queue orderDelayQueue() {
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 30*1000);
        arguments.put("x-dead-letter-exchange", QueueConstant.ORDER_DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", QueueConstant.ORDER_DEAD_KEY);
        return new Queue(QueueConstant.ORDER_DELAY_QUEUE, true, false, false, arguments);
    }

    @Bean
    public Queue orderDeadQueue() {
        return new Queue(QueueConstant.ORDER_DEAD_QUEUE);
    }

    @Bean
    public DirectExchange orderDeadExchange(){
        return new DirectExchange(QueueConstant.ORDER_DEAD_EXCHANGE);
    }

    @Bean
    public Binding orderDeadBinding(){
        return BindingBuilder.bind(orderDeadQueue()).to(orderDeadExchange()).with(QueueConstant.ORDER_DEAD_KEY);
    }
}
