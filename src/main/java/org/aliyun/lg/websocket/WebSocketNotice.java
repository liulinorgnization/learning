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
	 * �������¼�
	 * 
	 * @throws Exception
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam(value = "params") String paramStr) throws Exception {
		String comId = paramStr.substring(0, paramStr.indexOf("_"));
		String userId = paramStr.substring(paramStr.indexOf("_") + 1);
		log.info("�����ӳɹ���");
		sessionMap.put(paramStr, session);
		log.info("��˾��" + comId + "�û�" + userId + "�����ˡ�����");
		log.info("��ǰ����������" + sessionMap.size());

		// ��¼��Ϣ��ȡ��ǰ�û���Ҫ���ص�δ����Ϣ
		log.info("--------------userId:" + comId + "-loginId:" + userId + "-----------------");
		Map<String, Object> params = new HashMap<>();
		params.put("method", "weizhi.crmagent.otherService.querySysMessageByParameter");
		params.put("loginId", userId);
		params.put("comId", comId);
		//��һ�μ������е���Ϣ
		String msg = "����δ����Ϣ";
		session.getAsyncRemote().sendText(msg);
	}

	/**
	 * �ر������¼�
	 */
	@OnClose
	public void onClose(Session session, @PathParam(value = "params") String paramStr) {
		String userId = paramStr.substring(paramStr.indexOf("_") + 1);
		log.info("�ر����ӳɹ���");
		log.info("�û�" + userId + "�뿪�ˡ�����");
		sessionMap.remove(paramStr);
		log.info("��ǰ����������" + sessionMap.size());
	}

	/**
	 * ������Ϣ��Ӧ�¼�
	 * 
	 * @param session
	 * @param throwable
	 */
	@OnError
	public void OnError(Session session, Throwable throwable, @PathParam(value = "params") String paramStr) {
		log.info("�쳣��" + throwable.getMessage());
		log.info("�û�" +paramStr + "�����ӳ����˴��󡣡���");
		log.info("��ǰ����������" + sessionMap.size());
	}
}