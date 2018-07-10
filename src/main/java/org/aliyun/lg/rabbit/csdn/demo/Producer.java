package org.aliyun.lg.rabbit.csdn.demo;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;

/**
 * 
 * @Author: liulin
 * @Description: 描述
 * @Date: 2018年6月26日下午6:13:01
 */
public class Producer extends MyrabbitMqBase {

	public Producer(String mqName) throws IOException {
		super(mqName);
	}
	
	public void sendMessage(Serializable serializable) throws IOException{
		channel.basicPublish("",queueName , null, SerializationUtils.serialize(serializable));
	}

}
