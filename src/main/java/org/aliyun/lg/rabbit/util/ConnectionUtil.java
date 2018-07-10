package org.aliyun.lg.rabbit.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtil {

    public static Connection getConnection() throws Exception {
        //connection工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        //factory.setUsername("guest");
        //factory.setPassword("guest");
        factory.setVirtualHost("/");
        
        // 通过工厂获取连接
        Connection connection = factory.newConnection();
        return connection;
    }
}