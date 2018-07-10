package org.aliyun.lg.net.demo4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Junit4Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("127.0.0.1", 1111);
		Scanner scanner = new Scanner(System.in);
		OutputStream output = socket.getOutputStream();
		InputStream input = socket.getInputStream();
		while(true){
			String str = scanner.nextLine();
			output.write((str+"").getBytes());
			System.out.println(str);
			
			byte [] bodys = new byte[1024];
			String strs = "";
			while(input.read(bodys)>-1){
				strs = new String(bodys);
				System.out.println(strs);
			}
			System.out.println("ss"+strs);
		}
		
		
		
	}
}
