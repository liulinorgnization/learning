package org.aliyun.lg.workmodel.demo2;

public class Snippet {
	public static void main(String[] args) {
	    IAnimalFactory blackAnimalFactory = new BlackAnimalFactory();
	    ICat blackCat = blackAnimalFactory.createCat();
	    blackCat.eat();
	    IDog blackDog = blackAnimalFactory.createDog();
	    blackDog.eat();
	    
	    IAnimalFactory whiteAnimalFactory = new WhiteAnimalFactory();
	    ICat whiteCat = whiteAnimalFactory.createCat();
	    whiteCat.eat();
	    IDog whiteDog = whiteAnimalFactory.createDog();
	    whiteDog.eat();
	}
	
}

