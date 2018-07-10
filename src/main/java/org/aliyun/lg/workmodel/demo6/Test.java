package org.aliyun.lg.workmodel.demo6;
public class Test {
    
    public static void main(String[] args) {
    	ServiceA sa = new ServiceAImpl();
    	ServiceB sb = new ServiceBImpl();
        
        sa.methodA();
        sb.methodB();
        
        System.out.println("========");
        //facade
        Facade facade = new Facade();
        facade.methodA();
        facade.methodB();
    }
}
