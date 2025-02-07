package com.itheima.publisher.amqp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuhaokai
 * @description
 * @date 2025/2/6 14:50
 */
@Slf4j
@SpringBootTest
@Component
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSimpleQueue() {
        // 队列名称
        String queueName = "simple.queue";
        // 消息
        String message = "hello, spring amqp!";
        // 发送消息
        rabbitTemplate.convertAndSend(queueName, message);
    }

    @Test
    public void testWorkQueue() throws InterruptedException {
        // 队列名称
        String queueName = "work.queue";
        // 消息
        String message = "hello, message_";
        // 循环发送消息50次 每条消息间隔20ms
        for (int i = 0; i < 50; i++) {
            rabbitTemplate.convertAndSend(queueName, message + i);
            Thread.sleep(20);
        }
    }

    @Test
    public void testFanoutExchange() {
        // 交换机名称
        String exchangeName = "hmall.fanout";
        // 消息
        String message = "hello, everyone!";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "", message);
    }

    @Test
    public void testDirectExchange() {
        // 交换机名称
        String exchangeName = "hmall.direct";
        // 消息
        String message = "hello, red!";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "red", message);
    }

    @Test
    public void testTopicExchange() {
        // 交换机名称
        String exchangeName = "hmall.topic";
        // 消息
        String message = "hello, china!";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "japan.news", message);
    }

    @Test
    public void testSendMap() {
        String queueName = "object.queue";
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("name", "刘浩楷");
        messageMap.put("age", "18");
        messageMap.put("address", "东莞");
        rabbitTemplate.convertAndSend(queueName, messageMap);
    }

    /**
     * Publisher Confirm机制
     */
    @Test
    void testPublisherConfirm() {
        // 1.创建CorrelationData
        CorrelationData cd = new CorrelationData();
        // 2.给Future添加ConfirmCallback
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                // 2.1.Future发生异常时的处理逻辑，基本不会触发
                log.error("send message fail", ex);
            }
            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                // 2.2.Future接收到回执的处理逻辑，参数中的result就是回执内容
                // 1.消息到达交换机，入队失败，返回ack和路由异常信息
                // 2.消息到达交换机，入队成功，返回ack
                // 3.消息到达交换机，入队成功并完成持久化，返回ack
                // 4.消息没有到达交换机，返回nack
                if(result.isAck()){ // result.isAck()，boolean类型，true代表ack回执，false 代表 nack回执
                    log.debug("发送消息成功，收到 ack!");
                }else{ // result.getReason()，String类型，返回nack时的异常描述
                    log.error("发送消息失败，收到 nack, reason : {}", result.getReason());
                }
            }
        });
        // 3.发送消息
        rabbitTemplate.convertAndSend("hmall.direct", "q", "hello", cd);
    }

}
