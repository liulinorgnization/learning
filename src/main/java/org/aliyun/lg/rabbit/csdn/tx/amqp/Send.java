package org.aliyun.lg.rabbit.csdn.tx.amqp;

import com.rabbitmq.client.AMQP.Queue;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Send {

	
	
	public static void main(String[] args) {
		try {
			
			String exchange = "rabbitmq_txampq_test1";
			Connection connection = RabbitMqUtils.getInstance().getConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(exchange, "fanout");//derect topic
			String body = "往数据库insert 一条数据 ";
			String selectbody= "玩数据库select 一条数据";
			channel.txSelect();
			
			channel.basicPublish(exchange, "select", null, selectbody.getBytes());
			channel.basicPublish(exchange, "insert", null, body.getBytes());
			
			channel.txCommit();
			
			channel.close();
			connection.close();
			
			System.out.println("send end....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
