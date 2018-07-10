package org.aliyun.lg.net.demo2;
import java.io.*;
import java.net.*;
public class TestMark_to_win {
    public static final int PORT = 4002;
    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(PORT);
        // Blocks until a connection occurs:
        System.out.println("我作为服务器，正等着");
        Socket socket = s.accept();
        System.out.println("这句始打印不出来");
        InputStream in = socket.getInputStream();
        byte[] bodys = new byte[1024];
        while(in.read(bodys)>=0){
        	String strs = new String(bodys);
        	OutputStream output = socket.getOutputStream();
        	if(strs.equals("bye")){
        		output.write("bye,客 ".getBytes());
        		socket.close();
        		s.close();
        	}else{
        		output.write(("收到了客户信息："+strs).getBytes());
        	}
        	output.flush();
        	output.close();
        }
    }
}

