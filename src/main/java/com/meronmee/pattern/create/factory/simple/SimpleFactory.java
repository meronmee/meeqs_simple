package com.meronmee.pattern.create.factory.simple;

public class SimpleFactory {
	public static Fruit factory(String fruitName) throws Exception{
		if("apple".equals(fruitName)){
			return new Apple();
		} else if("banana".equals(fruitName)){
			return new Banana();
		} else {
			throw new Exception("Bad fruit");
		}
	}
	
	public static void main(String[] args) throws Exception{
		SimpleFactory.factory("apple").grow();
	}
}
