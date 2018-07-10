package org.aliyun.lg.codingmode.proxy.demo2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicProxy implements InvocationHandler {

	
	private ProxyInterface proxyInterface; 
	
	public DynamicProxy(ProxyInterface  proxyInterface) {
		this.proxyInterface = proxyInterface;
	}
	
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("开始..............");
		Object obj = method.invoke(proxyInterface, args);
		System.out.println("结束..............");
		return obj;
	}

}
