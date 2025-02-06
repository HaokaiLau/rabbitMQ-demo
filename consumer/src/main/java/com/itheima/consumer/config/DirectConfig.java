package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuhaokai
 * @description direct模式由于要绑定多个KEY，会非常麻烦，每一个Key都要编写一个binding
 * @date 2025/2/6 16:22
 */
//@Configuration
public class DirectConfig {

    /**
     * 声明direct交换机
     * @return Direct类型的交换机
     */
    @Bean
    public DirectExchange directExchange() {
        return ExchangeBuilder.directExchange("direct.exchange").build();
    }

    @Bean
    public Queue directQueue1() {
        return new Queue("direct.queue1");
    }

    @Bean
    public Queue directQueue2() {
        return new Queue("direct.queue2");
    }

    @Bean
    public Binding bindingQueue1WithRed(Queue directQueue1, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue1).to(directExchange).with("red");
    }

    @Bean
    public Binding bindingQueue1WithBlue(Queue directQueue1, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue1).to(directExchange).with("blue");
    }

    @Bean
    public Binding bindingQueue2WithYellow(Queue directQueue2, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue2).to(directExchange).with("yellow");
    }

    @Bean
    public Binding bindingQueue2WithRed(Queue directQueue2, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue2).to(directExchange).with("red");
    }

}
