package com.itheima.publisher.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author liuhaokai
 * @description
 * @date 2025/2/6 18:30
 */
@Slf4j
@AllArgsConstructor
@Configuration
public class MqConfig {

    private final RabbitTemplate rabbitTemplate;

    /**
     * publisher return机制
     * 该机制需要额外的网络和系统资源开销，尽量不要开启该机制，因为一般路由失败都是业务逻辑出现的问题
     */
    @PostConstruct
    public void init(){
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                log.error("触发return callback,");
                log.debug("exchange: {}", returned.getExchange());
                log.debug("routingKey: {}", returned.getRoutingKey());
                log.debug("message: {}", returned.getMessage());
                log.debug("replyCode: {}", returned.getReplyCode());
                log.debug("replyText: {}", returned.getReplyText());
            }
        });
    }

}
