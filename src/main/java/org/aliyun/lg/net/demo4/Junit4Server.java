package org.aliyun.lg.net.demo4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Junit4Server {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(1111);
		Socket socket=serverSocket.accept();
		while(true){
			// 保持长 
					try {
						Thread.sleep(100);// 等待时间
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					if (socket != null) {
						try {
							String ip = socket.getInetAddress().toString().replace("/", "");
							System.out.println("====socket.getInetAddress()=====" + ip);
							socket.setKeepAlive(true);
							InputStream is = socket.getInputStream();
							OutputStream os = socket.getOutputStream();
							System.out.println("服务器端接受请求");
							byte [] bodys = new byte[1024];
							String tempdata = "";
							while(is.read(bodys)>-1){
								tempdata =new String(bodys);
							}
							System.out.println("接收到的数据为：" + tempdata);
							if (tempdata.contains("stop")) {
								is.close();
								os.close();
							}
							os.flush();
						} catch (Exception e) {
							System.out.println("出现了错");
						}
					}
		}

	}
}
