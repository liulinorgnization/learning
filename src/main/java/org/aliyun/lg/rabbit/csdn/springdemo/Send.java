package org.aliyun.lg.rabbit.csdn.springdemo;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Send {
    
    public static void main(String[] args) throws InterruptedException {
        AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/rabbitmq-context.xml");
        
        //拿模板的bean
        //RabbitTemplate template = ctx.getBean(RabbitTemplate.class);
        //发消息
        String msg = "Hello world";
       // template.convertAndSend(msg);   //该函数还能指定routing-key
        
        Thread.sleep(1000);
        ctx.close();
    }
}