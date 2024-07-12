package com.bjpowernode.config;

import com.bjpowernode.consts.QueueConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue wxMsgQueue(){
        return new Queue(QueueConstant.WX_MSG_QUEUE);
    }
}
