package com.itheima.consumer.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author liuhaokai
 * @description
 * @date 2025/2/6 14:58
 */
@Component
public class SpringRabbitListener {
    // 利用RabbitListener来声明要监听的队列信息
    // 将来一旦监听的队列中有了消息，就会推送给当前服务，调用当前方法，处理消息。
    // 可以看到方法体中接收的就是消息体的内容
    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueueMessage(String msg) throws InterruptedException {
        System.out.println("spring 消费者接收到消息：【" + msg + "】" + LocalDateTime.now());
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue1(String msg) throws InterruptedException {
        System.out.println("消费者1接收到消息：【" + msg + "】" + LocalDateTime.now());
        Thread.sleep(20);
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue2(String msg) throws InterruptedException {
        System.out.println("消费者2......接收到消息：【" + msg + "】" + LocalDateTime.now());
        Thread.sleep(200);
    }

    @RabbitListener(queues = "fanout.queue1")
    public void listenFanoutQueue1(String msg) throws InterruptedException {
        System.out.println("消费者1接收到消息：【" + msg + "】" + LocalDateTime.now());
    }

    @RabbitListener(queues = "fanout.queue2")
    public void listenFanoutQueue2(String msg) throws InterruptedException {
        System.out.println("消费者2接收到消息：【" + msg + "】" + LocalDateTime.now());
    }

    @RabbitListener(queues = "direct.queue1")
    public void listenDirectQueue1(String msg) throws InterruptedException {
        System.out.println("消费者1接收到消息：【" + msg + "】" + LocalDateTime.now());
    }

    @RabbitListener(queues = "direct.queue2")
    public void listenDirectQueue2(String msg) throws InterruptedException {
        System.out.println("消费者2接收到消息：【" + msg + "】" + LocalDateTime.now());
    }

    @RabbitListener(queues = "topic.queue1")
    public void listenTopicQueue1(String msg) throws InterruptedException {
        System.out.println("消费者1接收到消息：【" + msg + "】" + LocalDateTime.now());
    }

    @RabbitListener(queues = "topic.queue2")
    public void listenTopicQueue2(String msg) throws InterruptedException {
        System.out.println("消费者2接收到消息：【" + msg + "】" + LocalDateTime.now());
    }

    /**
     * 通过注解来声明队列和交换机，并绑定队列到交换机
     * 路由键为 red 和 blue
     * 优点：direct模式由于要绑定多个KEY，会非常麻烦，每一个Key都要编写一个binding，使用注解来声明可以减少代码量
     *
     * @param msg
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "direct.queue1"),
                    exchange = @Exchange(name = "hmall.direct", type = ExchangeTypes.DIRECT),
                    key = {"red", "blue"}
            )
    )
    public void listenDirectQueue1WithConfig(String msg) {
        System.out.println("消费者1接收到direct.queue1的消息：【" + msg + "】");
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "direct.queue2"),
                    exchange = @Exchange(name = "hmall.direct", type = ExchangeTypes.DIRECT),
                    key = {"red", "yellow"}
            )
    )
    public void listenDirectQueue2WithConfig(String msg) {
        System.out.println("消费者2接收到direct.queue2的消息：【" + msg + "】");
    }
}
