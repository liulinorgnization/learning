package org.aliyun.lg.net.demo3;
import java.net.*;
import java.io.*;
public class Test {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 4002);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        		
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
/* autoFlush - A boolean; if true, the println() methods will flush the output buffer
。�?��?��?��?�更多内容，进入�?  http://www.mark-to-win.com/membership.html*/
        socket.close();
    }
}