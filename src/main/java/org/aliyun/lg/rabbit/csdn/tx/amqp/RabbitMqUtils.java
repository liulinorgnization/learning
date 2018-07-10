package org.aliyun.lg.rabbit.csdn.tx.amqp;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 
 * @author liuli
 *
 */
public class RabbitMqUtils {

	
	private static final RabbitMqUtils rabbitMqutil = new RabbitMqUtils();
	
	private RabbitMqUtils (){};
	
	public static RabbitMqUtils getInstance(){
		return rabbitMqutil;
	}
	
	public Connection getConnection() throws Exception{
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("localhost");
		connectionFactory.setPort(5672);
		connectionFactory.setUsername("admin");
		connectionFactory.setPassword("admin");
		connectionFactory.setVirtualHost("/");
		return connectionFactory.newConnection();
	}
}
