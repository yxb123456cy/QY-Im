package com.qy.im.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //配置Json序列化;
    @Bean
    public MessageConverter jacksonMessageConvertor() {
        return new Jackson2JsonMessageConverter();
    }
    // 死信队列名称
    public static final String DEAD_LETTER_QUEUE = "im_chat_dead_letter_queue";

    //普通交换机
    public static final String NORMAL_QUEUE = "im_chat_normal_queue";

    //路由键;
    public static final String DLX_ROUTING_KEY = "im.chat.dlx_routing_key";
    //死信交换机;
    public static final String DLX_EXCHANGE = "im.dlx_exchange";

    public static final String DIRECT_EXCHANGE = "im_chat_direct_exchange";
    public static final String ROUTING_KEY = "im.ws.mq";
    @Bean
    public TopicExchange dlxExchange() {
        return new TopicExchange(DLX_EXCHANGE);
    }
    //创建一个交换机;
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }
    //将交换机与queue进行绑定
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(normalQueue()).to(directExchange()).with(ROUTING_KEY);
    }

    @Bean
    public Queue normalQueue() {
        return QueueBuilder.durable(NORMAL_QUEUE)
                .withArgument("x-message-ttl", 60000) // 消息存活时间（5分钟）
                .withArgument("x-max-retries", 3) // 最大重试次数
                .build();
    }


}
