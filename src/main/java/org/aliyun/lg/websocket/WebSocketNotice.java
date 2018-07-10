package org.aliyun.lg.websocket;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;


@ServerEndpoint("/websocket/{params}")
public class WebSocketNotice {

	private Logger log = Logger.getLogger(WebSocketNotice.class);

	public static Map<String, Session> sessionMap = new HashMap<String, Session>();//

	/**
	 * 打开连接事件
	 * 
	 * @throws Exception
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam(value = "params") String paramStr) throws Exception {
		String comId = paramStr.substring(0, paramStr.indexOf("_"));
		String userId = paramStr.substring(paramStr.indexOf("_") + 1);
		log.info("打开连接成功！");
		sessionMap.put(paramStr, session);
		log.info("公司：" + comId + "用户" + userId + "进来了。。。");
		log.info("当前在线人数：" + sessionMap.size());

		// 登录消息获取当前用户需要加载的未读消息
		log.info("--------------userId:" + comId + "-loginId:" + userId + "-----------------");
		Map<String, Object> params = new HashMap<>();
		params.put("method", "weizhi.crmagent.otherService.querySysMessageByParameter");
		params.put("loginId", userId);
		params.put("comId", comId);
		//第一次加载所有的消息
		String msg = "所有未读消息";
		session.getAsyncRemote().sendText(msg);
	}

	/**
	 * 关闭连接事件
	 */
	@OnClose
	public void onClose(Session session, @PathParam(value = "params") String paramStr) {
		String userId = paramStr.substring(paramStr.indexOf("_") + 1);
		log.info("关闭连接成功！");
		log.info("用户" + userId + "离开了。。。");
		sessionMap.remove(paramStr);
		log.info("当前在线人数：" + sessionMap.size());
	}

	/**
	 * 错误信息响应事件
	 * 
	 * @param session
	 * @param throwable
	 */
	@OnError
	public void OnError(Session session, Throwable throwable, @PathParam(value = "params") String paramStr) {
		log.info("异常：" + throwable.getMessage());
		log.info("用户" +paramStr + "的连接出现了错误。。。");
		log.info("当前在线人数：" + sessionMap.size());
	}
}