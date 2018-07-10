package org.aliyun.lg.rabbit.csdn.demo5;

import org.aliyun.lg.rabbit.util.ConnectionUtil;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Send {

    private final static String EXCHANGE_NAME = "exchange_topic_ok3";

    public static void main(String[] argv) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        String message = "Hello world";
        // 发送消息, 指定RoutingKey
        channel.basicPublish(EXCHANGE_NAME, "item.delete", null, message.getBytes());

        channel.close();
        connection.close();
    }
}