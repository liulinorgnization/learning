package org.aliyun.lg.rabbit.csdn.demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * 
 * @Author: liulin
 * @Description:
 * @Date: 2018年6月26日下午6:12:17
 */
public class Consumer extends MyrabbitMqBase implements com.rabbitmq.client.Consumer, Runnable {

	@Override
	public void run() {
		try {
			channel.basicConsume(queueName, true,this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Consumer(String mqName) throws IOException {
		super(mqName);
	}

	@Override
	public void handleCancel(String s) throws IOException {
		System.out.println("handleCancel =>"+s);
	}

	@Override
	public void handleCancelOk(String s) {
		System.out.println("handleCancelOk.."+s);
	}

	@Override
	public void handleConsumeOk(String consumerTag) {
		System.out.println("Consumer " + consumerTag + " registered");
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope env, BasicProperties properties, byte[] body) throws IOException {
		Map map = (HashMap)SerializationUtils.deserialize(body);
		System.out.println("消费到->message:"+map.get("messagekey")+" handleDelivery end");
	}

	@Override
	public void handleRecoverOk(String s) {
	}

	@Override
	public void handleShutdownSignal(String s, ShutdownSignalException shutdownsignalexception) {
	}

}
