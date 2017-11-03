package com.meronmee.pattern.create.factory.factory;

public class ExportPdfFactory implements ExportFactory{
	public ExportFile factory(String dataType){
		if("deliver".equals(dataType)){
			return new ExportPdfDeliverFile();
		} else if("finance".equals(dataType)){
			return new ExportPdfFinanceFile();
		} else {
			return new ExportPdfFinanceFile();			
		}
	}
}
