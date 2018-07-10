package org.aliyun.lg.workmodel.demo7;

import java.util.HashMap;
import java.util.Map;

public class FlyweightFactory {

    private static Map flyweights = new HashMap<String,Flyweight>();
    
    public FlyweightFactory(String arg) {
        flyweights.put(arg, new FlyweightImpl());
    }
    
    public static Flyweight getFlyweight(String key) {
        if (flyweights.get(key) == null) {
            flyweights.put(key, new FlyweightImpl());
        }
        return (Flyweight) flyweights.get(key);
    }
    
    public static int getSize() {
        return flyweights.size();
    }
}
