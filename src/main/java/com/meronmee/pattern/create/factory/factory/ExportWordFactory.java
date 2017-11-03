package com.meronmee.pattern.create.factory.factory;

public class ExportWordFactory implements ExportFactory{
	public ExportFile factory(String dataType){
		if("deliver".equals(dataType)){
			return new ExportWordDeliverFile();
		} else if("finance".equals(dataType)){
			return new ExportWordFinanceFile();
		} else {
			return new ExportWordFinanceFile();			
		}
	}
}
