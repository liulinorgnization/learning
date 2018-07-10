package org.aliyun.lg.rabbit.csdn.demo2;

import org.aliyun.lg.rabbit.util.ConnectionUtil;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

public class Recv2 {

    private final static String QUEUE_NAME = "queue_work";
    
    //private final static String QUEUE_NAME = "";

    private final static boolean autoAck = false;
    
    public static void main(String[] argv) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。 
        //channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //channel.basicQos(1);
        //产生一个consumer
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //设置这个队列 给consumer 并设置消费模式 是否自动回收 autoAck
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
        while (true) {
        	// nextDelivery是一个阻塞方法（内部实现其实是阻塞队列的take方法）  
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [x]recv2获取 Received '" + message + "'");
            // 模拟handling
            Thread.sleep(200);
            // ACK
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            //channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
        }
    }
}