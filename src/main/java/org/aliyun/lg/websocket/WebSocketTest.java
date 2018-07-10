package org.aliyun.lg.websocket;
 
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket/{userId}")
public class WebSocketTest {
 
	public static Map<String, Session> sessionMap=new HashMap<String, Session>();//在线的客户端session集合，只在第一次new的时候初始化。
  /**
   * 接收信息事件
   * @param message 客户端发来的消息
   * @param session 当前会话
   */
  @OnMessage
  public void onMessage(String message,Session session,@PathParam(value="userId")String userId)throws Exception {
    try {
    	Iterator<String> it = sessionMap.keySet().iterator();
    	//循环给每个客户端发送信息
    	while(it.hasNext()){
    		String key = (String) it.next();
    		Session value = sessionMap.get(key);
    		value.getBasicRemote().sendText(message);
    	}
    	 System.out.println("用户"+userId+"说："+message+"。");
    	 System.out.println("当前在线人数："+sessionMap.size());
	} catch (Exception e) {
		System.out.println("接收消息事件异常!");
	}
  }
  
  /**
   * 打开连接事件
 * @throws Exception 
   */
  @OnOpen
  public void onOpen(Session session,@PathParam(value="userId")String userId) throws Exception {
    System.out.println("打开连接成功！");
    sessionMap.put(userId, session);
    if(userId.equals("8001")){
    	session.getAsyncRemote().sendText("没有读取的消息1");
    }
    System.out.println("用户"+userId+"进来了。。。");
    System.out.println("当前在线人数："+sessionMap.size());
  }
 
  /**
   * 关闭连接事件
   */
  @OnClose
  public void onClose(Session session,@PathParam(value="userId")String userId) {
    System.out.println("关闭连接成功！");
    System.out.println("用户"+userId+"离开了。。。");
    sessionMap.remove(userId);
    System.out.println("当前在线人数："+sessionMap.size());
  }
  
  /**
   * 错误信息响应事件
   * @param session
   * @param throwable
   */
  @OnError
  public void OnError(Session session,Throwable throwable,@PathParam(value="userId")String userId) {
	    System.out.println("异常："+throwable.getMessage());
	    System.out.println("用户"+userId+"的连接出现了错误。。。");
	    System.out.println("当前在线人数："+sessionMap.size());
  }
  
}