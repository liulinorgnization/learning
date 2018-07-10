package org.aliyun.lg.rabbit.csdn.demo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * 
 * @Author: liulin
 * @Description: rabbitMq单节点 官网写法 
 * 实现consumer接口
 * @Date: 2018年6月27日上午11:28:27
 */
public class Test {
	public Test() throws Exception {
		Consumer consumer = new Consumer("queue");  
        Thread consumerThread = new Thread(consumer);  
        consumerThread.start();  

		Producer producer = new Producer("queue");
		HashMap map = new HashMap<>();
		map.put("messagekey", "data:{\"message\":\"提交成功!\"}");
		producer.sendMessage(map);

	}

	/**
	 * @param args
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception {
		new Test();
	}
}