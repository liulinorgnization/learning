package org.aliyun.lg.rabbit.csdn;


/**
 * 
 * @author liuli
 *
 */
public interface packageinfo {
	/**
	 * 基本概念
	Queue
	Exchange
	Channel
	Connection
	Ack
	Exchange 的几种类型和使用场景
	不指定exchange 
	Direct默认 需要指定queue 往哪里发 典型的点对点
	Fanout 就是发送给大伙 大家都有 根据baingding 的exchange 获取消息
	（其中一个小细节 先创建1、exchange 2、消费方创建queue 绑定关系 3、 发送消息 ）才能获取到消息
	Topic 这个没啥好说的 就是一个通配符 匹配模式和fanout 类似  *.*  key.#
	消息没有并任何一个consumer ack 掉就会一直重发（这里注意代码逻辑一定要ack 掉不要造成mq 阻塞）
	 */

}
