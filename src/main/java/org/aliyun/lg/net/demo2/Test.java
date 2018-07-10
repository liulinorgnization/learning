package org.aliyun.lg.net.demo2;
import java.net.*;
import java.util.Scanner;
import java.io.*;
public class Test {
    public static void main(String[] args) throws IOException {
    	Socket socket = new Socket("localhost", 4002);
    	Scanner s = new Scanner(System.in);
    	System.out.println("请输入：");
    	while(true){
    		String nStr = s.nextLine();
            OutputStream out = socket.getOutputStream();
            out.write(nStr.getBytes());
            InputStream input =	socket.getInputStream();
            byte[] bodys = new byte[1024];
            while(input.read(bodys)>0){
            	System.out.println(new String(bodys));
            }
            if(nStr.equals("bye")){
    			break;
    		}
    	}
    	socket.close();
    }
	
}

