package org.aliyun.lg.net.demo3;
import java.io.*;
import java.net.*;
public class TestMark_to_win {
    public static final int PORT = 4002;
    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("服务器正等着");
        // Blocks until a connection occurs:
        Socket socket = s.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        /* autoFlush - A boolean; if true, the println() methods will flush the output buffer*/
        PrintWriter out = new PrintWriter((new OutputStreamWriter(
                socket.getOutputStream())), true);
        String str = in.readLine();
        System.out.println("Echoing: " + str);
        out.println(str + "回来从服务器");
        // Always close the two sockets...
        System.out.println("closing...");
        socket.close();
        s.close();
    }
}