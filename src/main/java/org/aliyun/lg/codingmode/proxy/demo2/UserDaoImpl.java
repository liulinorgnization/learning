package org.aliyun.lg.codingmode.proxy.demo2;

public class UserDaoImpl implements ProxyInterface {

	
	public String queryUserList(){
		System.out.println("一段乱码");
		return "{userName:'zs',sex:'男'}";
	}
}
