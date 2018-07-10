package org.aliyun.lg.codingmode.proxy.demo2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class test {

	
	public static void main(String[] args) throws Exception {
		/*ProxyInterface proxyInterface = new UserDaoImpl();
		
		InvocationHandler invocationHandler = new DynamicProxy(proxyInterface);
		
		ProxyInterface proxy = (ProxyInterface) Proxy.newProxyInstance(invocationHandler.getClass().getClassLoader(),
				proxyInterface.getClass().getInterfaces(), invocationHandler);
		
		String result =	proxy.queryUserList();
		System.out.println(result);*/
		
		Class classs =Class.forName("org.aliyun.lg.proxy.demo2.UserDaoImpl");
		Object o = classs.newInstance();
		
		for(int i=0;i<classs.getMethods().length;i++){
			java.lang.reflect.Method method = classs.getMethods()[i];
			if(method.getName().equals("queryUserList")){
				Object result = method.invoke(o, args);
				System.out.println(result);	
			}
		}

	}
}
