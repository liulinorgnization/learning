package org.aliyun.lg.rabbit.csdn.demo4;

import org.aliyun.lg.rabbit.util.ConnectionUtil;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
public class Send {

    private final static String EXCHANGE_NAME = "exchange_direct_ok";

    public static void main(String[] argv) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        // 声明exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        try {
        	channel.txSelect();
        	for(int i=0;i<4;i++){
            	if(i==3){
            		int result  = 1/0;
            	}
	            String message = "Hello world 事务";
	            // 发送消息, RoutingKey为 insert
	            channel.basicPublish(EXCHANGE_NAME, "insert", null, (message+i).getBytes());
	            channel.txCommit();
        	}
		} catch (Exception e) {
			channel.txRollback();
			System.out.println("回滚。。");
		}
        channel.close();
        connection.close();
    }
}