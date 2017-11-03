package com.meronmee.pattern.create.factory.factory;

import com.meronmee.app.Log;

public class ExportWordDeliverFile implements ExportFile{

	public void export(String data){
		Log.info("export ExportWordDeliverFile:" + data);		
	}
}
