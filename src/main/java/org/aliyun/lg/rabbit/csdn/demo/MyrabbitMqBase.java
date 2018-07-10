package org.aliyun.lg.rabbit.csdn.demo;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 
 * @Author: liulin
 * @Description: 描述
 * @Date: 2018年6月26日下午6:12:44
 */
public abstract class MyrabbitMqBase {

	protected String queueName;
	
	protected Channel channel;
	
	protected Connection connection;
	
	public MyrabbitMqBase(String mqName) throws IOException{
		this.queueName = mqName;
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("127.0.0.1");
		connectionFactory.setPort(5672);
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		connection = connectionFactory.newConnection();
		channel = connection.createChannel();
		channel.queueDeclare(mqName, false, false, false, null);
		
	}
}
