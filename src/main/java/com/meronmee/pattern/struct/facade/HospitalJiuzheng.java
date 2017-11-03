package com.meronmee.pattern.struct.facade;

import com.meronmee.app.Log;

/**
 * 就诊
 * @author Meron
 *
 */
public class HospitalJiuzheng {
	
	public void jiancha(){
		Log.info("检查..");
	}
	public void chouxie(){
		Log.info("抽血...");
	}
	public void chankanchouxiejieguo(){
		Log.info("查看抽血结果...");
	}
	public void kaiyaofang(){
		Log.info("开药方...");
	}
}
