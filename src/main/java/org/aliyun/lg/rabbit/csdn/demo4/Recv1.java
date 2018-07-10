package org.aliyun.lg.rabbit.csdn.demo4;

import org.aliyun.lg.rabbit.util.ConnectionUtil;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

public class Recv1 {

    private final static String QUEUE_NAME = "queue_direct_1";
    private final static String EXCHANGE_NAME = "exchange_direct_ok";

    public static void main(String[] argv) throws Exception {

        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 绑定队列到交换机, BindingKey为 delete update
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "update");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "delete");

        channel.basicQos(1);
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, false, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println("获取:" + message);
            Thread.sleep(100);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }
}