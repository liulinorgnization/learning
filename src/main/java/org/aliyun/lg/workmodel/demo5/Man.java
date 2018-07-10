package org.aliyun.lg.workmodel.demo5;
public class Man extends Person {
    
    public Man() {
        setType("男人");
    }
    
    public void dress() {
        Clothing clothing = getClothing();
        clothing.personDressCloth(this);
    }
}
