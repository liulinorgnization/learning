package org.aliyun.lg.net.demo1;
import java.net.*;
public class TestMark_to_win {
    public static void main(String[] args) throws Exception {
        /* static InetAddress getByName(String host) Determines the IP address
of a host, given the host's name.
         */
    	InetAddress[] ias = InetAddress.getAllByName("www.baidu.com");
    	
    	for(int i=0;i<ias.length;i++){
    		System.out.println(ias[i].getHostAddress());
    	}
        InetAddress a = InetAddress.getByName("localhost");
        System.out.println(a.getHostAddress());
        System.out.println(a.getHostName());
        InetAddress b = InetAddress.getByName("www.baidu.com");
        System.out.println(b.getHostAddress());
        System.out.println(b.getHostName());
    }
}