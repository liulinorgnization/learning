package org.aliyun.lg.rabbit.csdn.demo3;

import org.aliyun.lg.rabbit.util.ConnectionUtil;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Send {
	
	private final static String EXCHANGE_NAME = "exchange_fanout";

	public static void main(String[] argv) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明exchange  发送到交换机exchange 和 上述1、2事例不一样
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        // 消息内容
        String message = "Hello world shiwu";
        
        try {
        	channel.txSelect();
        	  for(int i=0;i<4;i++){
              	 // 与前面不同, 生产者将消息发送给exchange, 而非队列. 若发消息时还没消费者绑定queue与该exchange, 消息将丢失
                  channel.basicPublish(EXCHANGE_NAME, "", null, (message+i).getBytes());
                  if(i==3){
                  	int result = 1/0;
                  }
              }
        	  channel.txCommit();
              channel.close();
              connection.close();
		} catch (Exception e) {
			channel.txRollback();
			System.out.println("事务回滚");
		}
        System.out.println("over");
    }
}