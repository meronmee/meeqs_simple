package com.meronmee.pattern.create.factory.factory;

public class Test {

	public static void main(String[] args) {
		ExportFactory factory = new ExportWordFactory();
		ExportFile file = factory.factory("finance");
		file.export("data");
				
	}

}
