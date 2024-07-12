package com.bjpowernode.config;

import com.bjpowernode.consts.QueueConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue esChangeQueue(){
        return new Queue(QueueConstant.ES_CHANGE_QUEUE);
    }
}
