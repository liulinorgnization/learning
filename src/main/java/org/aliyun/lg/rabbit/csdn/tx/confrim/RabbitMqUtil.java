package org.aliyun.lg.rabbit.csdn.tx.confrim;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 
 * @author liuli
 *
 */
public class RabbitMqUtil {
	
	
	private static final RabbitMqUtil  rabbitmqUtil = new RabbitMqUtil();
	
	private RabbitMqUtil(){};
	
	public RabbitMqUtil getInstance(){
		return rabbitmqUtil;
	}
	
	/**
	 * 
	 * @return
	 */
	public Connection getConnection(){
		ConnectionFactory connectionFactory = new ConnectionFactory();
		Connection connection = null;
		try {
			connectionFactory.setHost("localhost");
			connectionFactory.setPort(5672);
			connectionFactory.setUsername("admin");
			connectionFactory.setPassword("admin");
			connectionFactory.setVirtualHost("/");
			connection = connectionFactory.newConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	
}
