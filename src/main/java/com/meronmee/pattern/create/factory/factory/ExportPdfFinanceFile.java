package com.meronmee.pattern.create.factory.factory;

import com.meronmee.app.Log;

public class ExportPdfFinanceFile implements ExportFile{
	public void export(String data){
		Log.info("export ExportPdfFinanceFile:" + data);		
	}
}
