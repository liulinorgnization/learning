package org.aliyun.lg.rabbit.csdn.demo2;

import org.aliyun.lg.rabbit.util.ConnectionUtil;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

public class Recv1 {

    private final static String QUEUE_NAME = "queue_work";

    public static void main(String[] argv) throws Exception {

        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //默认指定 queue key
        //channel.exchangeDeclare("", "direct", true);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 开启Qos, 同一时刻服务器只发送一条消息. 可以尝试注释该行, 会发现消息会被平均分配给两个消费者
        //个人理解 从服务器每次拉取消息的最大值
        channel.basicQos(1);

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, false, consumer);
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println("recv1 获取:" + message);
            // 模拟handling
            Thread.sleep(100);
            // 手动确认消息接收. 在basicConsume方法中, true为自动, false为手动
            /* 消息确认方式: 
             * 1. 自动确认. 只要消息从队列中移除, 服务端认为消息被成功消费
             * 2. 手动确认. 消费者获取消息后, 服务器将该消息标记为不可用, 并等待反馈. 
             * 如果消费者一直不反馈, 则该消息将一直处于不可用状态
             */
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }
}