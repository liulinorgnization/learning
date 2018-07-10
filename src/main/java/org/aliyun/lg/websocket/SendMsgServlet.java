package org.aliyun.lg.websocket;

import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

/**
 * 
 * @author liu.lin
 * @date 2018年1月17日 下午5:09:51 
 * @Description:  
 *
 */
public class SendMsgServlet extends HttpServlet {

	private static final long serialVersionUID = -2647143790914553060L;

	// 直系通知
	public static final String msgTemplet_beeline = "客户名称释放成功！";
	// 被审核通知模板
	public static final String msgTemplet_quiltverify = "";
	// 已审核通知模板
	public static final String msgTemplet_alreadyverify = "";

	/**
	 * 保存并推送消息
	 * 
	 * @param req
	 * @param resp
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String saveAndPushSystemMsg(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> param)
			throws Exception {
		// 触发给当前负责人id session 的消息
		Session session = WebSocketNotice.sessionMap.get(("55" + "_" + "张三"));
		session.getAsyncRemote().sendText("消息");
		return "";
	}

}
