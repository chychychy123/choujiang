package org.chy.award.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {

    // 定义交换机
    @Bean
    public TopicExchange drawTopicExchange() {
        return new TopicExchange("draw.topic");
    }

    // 定义队列
    @Bean
    public Queue drawSuccessQueue() {
        return new Queue("draw.success.queue", true); // durable = true
    }

    // 定义绑定
    @Bean
    public Binding bindingDrawSuccessQueue(Queue drawSuccessQueue, TopicExchange drawTopicExchange) {
        return BindingBuilder.bind(drawSuccessQueue).to(drawTopicExchange).with("draw.success");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        // 1.定义消息转换器
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // 注册JavaTimeModule来处理LocalDateTime
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
