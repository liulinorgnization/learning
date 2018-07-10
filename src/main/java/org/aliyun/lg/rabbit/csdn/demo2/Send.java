package org.aliyun.lg.rabbit.csdn.demo2;

import org.aliyun.lg.rabbit.util.ConnectionUtil;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Send {

    private final static String QUEUE_NAME = "queue_work";
    
    public static void main(String[] argv) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
       // channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        for (int i = 0; i < 3; i++) {
        	System.out.println("start ...");
            String message = "发送消息" + i;
            //默认exchange = direct 指定明确queueName
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

            Thread.sleep(i * 10);
            System.out.println("end ...");
        }

        channel.close();
        connection.close();
        
        System.out.println("over...");
    }
}