package org.aliyun.lg.rabbit.csdn.demo1;

import java.io.IOException;

import javax.swing.plaf.SliderUI;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Register {

	private static final String QUEUE_NAME = "hello";
	
	public static void main(String[] args) throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		
		connectionFactory.setHost("localhost");
		Connection connection =	connectionFactory.newConnection();
		
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");  
		
		QueueingConsumer consumer = new QueueingConsumer(channel);
		
		channel.basicConsume(QUEUE_NAME, true, consumer);
		
		while (true)  
        {  
            //nextDelivery是一个阻塞方法（内部实现其实是阻塞队列的take方法）  
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();  
            String message = new String(delivery.getBody());  
            System.out.println(" [x] Received '" + message + "'");  
            Thread.sleep(2000);
        }  
	}
}
