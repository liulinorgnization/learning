package org.aliyun.lg.workmodel.demo2;
public class WhiteAnimalFactory implements IAnimalFactory {

    public ICat createCat() {
        return new WhiteCat();
    }

    public IDog createDog() {
        return new WhiteDog();
    }

}
