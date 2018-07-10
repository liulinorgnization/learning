package org.aliyun.lg.rabbit.csdn.tx.amqp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class Consumer {

	
	
	public static void main(String[] args) {
		try {
			String queueName = "rabbitmq_txampq_queue01";
			String exchangekey = "rabbitmq_txampq_test1";
			Connection connection =	RabbitMqUtils.getInstance().getConnection();
			Channel channel = connection.createChannel();
			
			channel.queueDeclare(queueName, false, false, false, null);
			channel.queueBind(queueName, exchangekey, "");
			
			QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, false, queueingConsumer);
			while(true){
				Delivery delivery =	queueingConsumer.nextDelivery();
				String message = new String(delivery.getBody());
				System.out.println("获得消息："+message);
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
				Thread.sleep(2000);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
